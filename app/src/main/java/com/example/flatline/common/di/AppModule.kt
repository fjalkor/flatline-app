package com.example.flatline.common.di

import android.util.Log
import com.example.flatline.Config
import com.example.flatline.common.datastore.Store
import com.example.flatline.features.archivedlistings.ArchivedListingsViewModel
import com.example.flatline.features.archivedlistings.datasource.ArchivedListingsRepository
import com.example.flatline.features.listings.ListingsViewModel
import com.example.flatline.features.listings.datasource.ListingsRepository
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

@OptIn(ExperimentalSerializationApi::class)
val appModule = module {
    single<retrofit2.Converter.Factory> {
        val json = Json { ignoreUnknownKeys = true }

        json.asConverterFactory("application/json".toMediaType())
    }

    single<Store> { Store(androidContext()) }
    single<Retrofit> {
        val store = get<Store>()

        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val token = store.getFcmTokenBlocking()
                val request = chain.request().newBuilder()
                    .addHeader("FCM-Token", token)
                    .build()
                Log.d("FCM", "add token to request: $token")
                Log.d("FCM", "interceptor: $request")
                chain.proceed(request)
            }
            .build()

        Retrofit.Builder()
            .baseUrl(Config.environment.value.baseUrl)
            .client(client)
            .addConverterFactory(get())
            .build()
    }
    singleOf(::ListingsRepository)
    singleOf(::ArchivedListingsRepository)
    viewModel { ListingsViewModel(get(), get(), get()) }
    viewModel { ArchivedListingsViewModel(get(), get(), get()) }
}