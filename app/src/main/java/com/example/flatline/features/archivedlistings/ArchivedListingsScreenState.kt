package com.example.flatline.features.archivedlistings

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.flatline.features.archivedlistings.model.ArchivedListing
import com.example.flatline.features.archivedlistings.model.ArchivedListings
import com.example.flatline.features.listings.Snack
import kotlinx.coroutines.flow.MutableSharedFlow

class ArchivedListingsScreenState(
    val snackbarFlow: MutableSharedFlow<Snack> = MutableSharedFlow(),
    val isRefreshing: MutableState<Boolean> = mutableStateOf(false),
    val listingsBeforeRemoval: MutableState<List<ArchivedListing>> = mutableStateOf(emptyList()),
    val listings: MutableState<List<ArchivedListing>> = mutableStateOf(emptyList()),
    val snackbarState: SnackbarHostState = SnackbarHostState(),
) {
    fun update(new: ArchivedListings) {
        listings.value = new.items
    }
}