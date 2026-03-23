package com.example.flatline.features.listings

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flatline.common.datastore.Store
import com.example.flatline.common.extensions.observeSnacks
import com.example.flatline.features.listings.datasource.ListingsRepository
import com.example.flatline.features.registerdevice.RegisterDeviceApi
import com.example.flatline.features.registerdevice.remote.model.RegisterDeviceRequestBody
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Retrofit

class ListingsViewModel(
    private val store: Store,
    private val repository: ListingsRepository,
    private val retrofit: Retrofit,
) : ViewModel() {
    val state = ListingsScreenState()
    private val registerDeviceApi: RegisterDeviceApi
        get() = retrofit.create(RegisterDeviceApi::class.java)

    private var refreshJob: Job? = null

    init {
        observeSnacks()

        FirebaseMessaging.getInstance().token.addOnSuccessListener { token ->
            Log.d("FCM", "Found a token!: $token")
            runBlocking {
                store.setFcmToken(token)
                Log.d("FCM", "saved token")
            }
            viewModelScope.launch {
                runCatching {
                    registerDeviceApi.registerDevice(
                        RegisterDeviceRequestBody(
                            token,
                            "and"
                        )
                    )
                }
                    .onSuccess { refresh() }
                    .onFailure { state.snackbarFlow.emit(Snack.Error("failed to register device")) }
            }
        }
    }

    fun hideListing(id: Int) {
        state.listings.value = state.listings.value.filter { it.id != id }
        viewModelScope.launch {
            runCatching { repository.hideListingWithId(id) }
                .onFailure {
                    Log.w("ListingsViewModel", "failed to hide listing with id = $id: $it")
                    state.snackbarFlow.emit(Snack.Error("Failed to remove this listing. Try again later :-("))
                }
        }
    }

    fun onRefresh() {
        Log.i("ListingsViewModel", "requesting refresh!")
        refresh()
    }

    private fun refresh() {
        if (state.isRefreshing.value) return
        refreshJob?.cancel()

        state.isRefreshing.value = true
        refreshJob = viewModelScope.launch {
            runCatching { repository.getListings() }
                .onSuccess {
                    state.update(it)
                    state.snackbarFlow.emit(Snack.Info("updated"))
                }
                .onFailure {
                    Log.i("ListingsViewModel", "failed to fetch listings: $it")
                    state.snackbarFlow.emit(Snack.Error("Failed to fetch listings. Try again later :-("))
                }
            state.isRefreshing.value = false
        }
    }

    private fun observeSnacks() = viewModelScope.launch {
        state.snackbarState.observeSnacks(state.snackbarFlow)
    }
}