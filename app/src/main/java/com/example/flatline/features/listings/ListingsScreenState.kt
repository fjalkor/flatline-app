package com.example.flatline.features.listings

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.flatline.features.listings.domain.Listing
import com.example.flatline.features.listings.domain.Listings
import kotlinx.coroutines.flow.MutableSharedFlow

class ListingsScreenState(
    val isRefreshing: MutableState<Boolean> = mutableStateOf(false),
    val lastUpdatedAt: MutableState<String?> = mutableStateOf(null),
    val listings: MutableState<List<Listing>> = mutableStateOf(emptyList()),
    val snackbarState: SnackbarHostState = SnackbarHostState(),
    val snackbarFlow: MutableSharedFlow<Snack> = MutableSharedFlow(),
) {
    fun update(new: Listings) {
        listings.value = new.items
        lastUpdatedAt.value = new.lastUpdatedAt
    }
}
