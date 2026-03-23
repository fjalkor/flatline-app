package com.example.flatline.common.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import kotlin.text.orEmpty

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "flatline_data_store_prefs")

class Store(private val context: Context) {
    val FCM_TOKEN_KEY = stringPreferencesKey("fcm_token_key")

    fun getFcmToken(): Flow<String> = context.dataStore.data.map { it[FCM_TOKEN_KEY].orEmpty() }
    fun getFcmTokenBlocking(): String = runBlocking {
        context.dataStore.data.map { it[FCM_TOKEN_KEY].orEmpty() }.first()
    }

    suspend fun setFcmToken(token: String) {
        context.dataStore.updateData { it.toMutablePreferences().also { prefs -> prefs[FCM_TOKEN_KEY] = token } }
    }
}
