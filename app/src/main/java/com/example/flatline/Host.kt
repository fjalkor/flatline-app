package com.example.flatline

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.flatline.features.archivedlistings.ArchivedListingsScreen
import com.example.flatline.features.archivedlistings.ArchivedListingsViewModel
import com.example.flatline.features.lisitingdetails.ListingDetailsScreen
import com.example.flatline.features.listings.ListingsScreen
import com.example.flatline.features.listings.ListingsViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun Host(
    startDestination: String? = null,
    listingsViewModel: ListingsViewModel = koinViewModel(),
    archivedListingsViewModel: ArchivedListingsViewModel = koinViewModel(),
    onBack: () -> Unit,
) {
    val navHostController = rememberNavController()
    val currentDestination = navHostController.currentBackStackEntryAsState()
    val navBarHeightDp = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    LaunchedEffect(listingsViewModel.state.listings.value) {
        archivedListingsViewModel.onRefresh()
    }

    LaunchedEffect(archivedListingsViewModel.state.listings.value) {
        listingsViewModel.onRefresh()
    }

    val isBottomNavVisible = remember { mutableStateOf(true) }

    val scrollConnection: NestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                val threshold = 4
                when {
                    consumed.y < -threshold -> isBottomNavVisible.value = false
                    consumed.y > threshold -> isBottomNavVisible.value = true
                }

                return super.onPostScroll(consumed, available, source)
            }
        }
    }

    Box {
        NavHost(
            navController = navHostController,
            startDestination = startDestination ?: Routes.Listings.route,
        ) {
            composable(
                route = Routes.Listings.route,
                exitTransition = { slideOutHorizontally { -it } },
                enterTransition = { slideInHorizontally { -it } },
            ) {
                ListingsScreen(
                    state = listingsViewModel.state,
                    onRefresh = { listingsViewModel.onRefresh() },
                    onListingClicked = { url -> navHostController.navigate("${Routes.Details.route}/" + url) },
                    onListingDismissed = { id -> listingsViewModel.hideListing(id) },
                    onBack = onBack,
                    scrollConnection = scrollConnection,
                )
            }

            composable(
                route = Routes.ArchivedListings.route,
                exitTransition = { slideOutHorizontally { it } },
                enterTransition = { slideInHorizontally { it } },
            ) {
                ArchivedListingsScreen(
                    state = archivedListingsViewModel.state,
                    onRefresh = { archivedListingsViewModel.onRefresh() },
                    onListingClicked = { url -> navHostController.navigate("${Routes.Details.route}/" + url) },
                    onListingDismissed = { id -> archivedListingsViewModel.unhideListing(id) },
                    onBack = onBack,
                    scrollConnection = scrollConnection,
                )
            }

            composable(
                route = "${Routes.Details.route}/{url}",
                arguments = listOf(navArgument("url") { type = NavType.StringType }),
                enterTransition = { slideInHorizontally { it } },
            ) { entry ->
                val goBack = { navHostController.navigate(Routes.Listings.route) }

                entry.arguments?.getString("url")
                    ?.let { ListingDetailsScreen(it, onBack = goBack) }
                    ?: goBack()
            }
        }

        AnimatedVisibility(
            modifier = Modifier.align(Alignment.BottomCenter),
            visible = isBottomNavVisible.value && currentDestination.value?.destination?.route in Routes.entries.map { it.route },
            enter = slideInVertically { it },
            exit = slideOutVertically { it },
        ) {
            PrimaryTabRow(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .padding(bottom = navBarHeightDp),
                selectedTabIndex = when (currentDestination.value?.destination?.route) {
                    Routes.Listings.route -> 0
                    Routes.ArchivedListings.route -> 1
                    else -> 0
                },
                divider = {},
            ) {
                Tab(
                    modifier = Modifier.background(MaterialTheme.colorScheme.background),
                    selected = currentDestination.value?.destination?.route == Routes.Listings.route,
                    onClick = { navHostController.navigate(Routes.Listings.route) },
                    text = { Text(text = "All") },
                )

                Tab(
                    modifier = Modifier.background(MaterialTheme.colorScheme.background),
                    selected = currentDestination.value?.destination?.route == Routes.ArchivedListings.route,
                    onClick = { navHostController.navigate(Routes.ArchivedListings.route) },
                    text = { Text(text = "Archived") },
                )
            }
        }
    }
}
