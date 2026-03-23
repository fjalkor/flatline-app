package com.example.flatline.features.listings

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp

@Composable
fun ListingsScreen(
    state: ListingsScreenState,
    onRefresh: () -> Unit,
    onListingClicked: (String) -> Unit,
    onListingDismissed: (Int) -> Unit,
    onBack: () -> Unit,
    scrollConnection: NestedScrollConnection = object: NestedScrollConnection {},
) {
    BackHandler { onBack() }
    val statusBarHeightDp = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

    PullToRefreshBox(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background),
        isRefreshing = state.isRefreshing.value,
        onRefresh = onRefresh,
        contentAlignment = Alignment.TopCenter,
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxHeight()
                .padding(horizontal = 16.dp)
                .nestedScroll(scrollConnection),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item(key = "spacer") { Spacer(Modifier.height(48.dp)) }

            state.lastUpdatedAt.value?.let {
                item(key = "last_updated_at") {
                    Text(
                        text = "last_updated_at: $it",
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                }
            }

            items(items = state.listings.value, key = { it.id }) {
                ListingView(
                    modifier = Modifier.animateItem(),
                    item = it,
                    onClick = { onListingClicked(it.url) },
                    onDismiss = { onListingDismissed(it.id) }
                )
            }

            item(key = "footer") {
                Spacer(Modifier.height(32.dp))
            }
        }

        SnackbarHost(
            modifier = Modifier.padding(top = statusBarHeightDp),
            hostState = state.snackbarState,
        ) { data ->
            when (data) {
                is Snack.Error -> Snackbar(
                    snackbarData = data,
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError,
                )
                else -> Snackbar(
                    snackbarData = data,
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                )
            }
        }
    }
}
