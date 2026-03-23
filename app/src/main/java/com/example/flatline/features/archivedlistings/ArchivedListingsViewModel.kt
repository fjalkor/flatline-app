package com.example.flatline.features.archivedlistings

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flatline.common.datastore.Store
import com.example.flatline.common.extensions.observeSnacks
import com.example.flatline.features.archivedlistings.datasource.ArchivedListingsRepository
import com.example.flatline.features.listings.Snack
import com.example.flatline.features.registerdevice.RegisterDeviceApi
import com.example.flatline.features.registerdevice.remote.model.RegisterDeviceRequestBody
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Retrofit

class ArchivedListingsViewModel(
    private val store: Store,
    private val repository: ArchivedListingsRepository,
    private val retrofit: Retrofit,
) : ViewModel() {
    val state = ArchivedListingsScreenState()

    private val registerDeviceApi: RegisterDeviceApi
        get() = retrofit.create(RegisterDeviceApi::class.java)
    var refreshJob: Job? = null

    init {
        observeSnacks()

        FirebaseMessaging.getInstance().token.addOnSuccessListener { token ->
            Log.d("FCM", "Found a token!: $token")
            runBlocking {
                store.setFcmToken(token)
                Log.d("FCM", "saved token")
            }
            viewModelScope.launch {
                runCatching { registerDeviceApi.registerDevice(RegisterDeviceRequestBody(token, "and")) }
                    .onSuccess { refresh() }
                    .onFailure { state.snackbarFlow.emit(Snack.Error("failed to register device")) }
            }
        }
    }

    private fun observeSnacks() = viewModelScope.launch {
        state.snackbarState.observeSnacks(state.snackbarFlow)
    }

    fun onRefresh() {
        Log.i("ArchivedListingsViewModel", "requesting refresh!")
        refresh()
    }

    fun unhideListing(id: Int) {
        state.listings.value = state.listings.value.filter { it.id != id }
        viewModelScope.launch {
            runCatching { repository.unhideListingWithId(id) }
                .onFailure {
                    Log.w("ArchivedListingsViewModel", "failed to unhide listing with id = $id: $it")
                    state.snackbarFlow.emit(Snack.Error("Failed to bring back this listing. Try again later :-("))
                }
        }
    }

    private fun refresh() {
        if (state.isRefreshing.value) return

        refreshJob?.cancel()

        state.isRefreshing.value = true
        refreshJob = viewModelScope.launch {
            runCatching { repository.getArchivedListings() }
                .onSuccess {
                    state.update(it)
                    state.snackbarFlow.emit(Snack.Info("updated"))
                }
                .onFailure {
                    Log.i("ArchivedListingsViewModel", "failed to fetch archived listings: $it")
                    state.snackbarFlow.emit(Snack.Error("Failed to fetch archived listings. Try again later :-("))
                }
            state.isRefreshing.value = false
        }
    }
}