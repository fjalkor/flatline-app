package com.example.flatline.features.listings.datasource

import com.example.flatline.features.listings.datasource.extensions.toListings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit

class ListingsRepository(private val retrofit: Retrofit) {
    fun api(): ListingsApi = retrofit.create(ListingsApi::class.java)

    suspend fun getListings() = withContext(Dispatchers.IO) {
        api().getListings().toListings()
    }

    suspend fun hideListingWithId(id: Int) = withContext(Dispatchers.IO) {
        api().hideListingWithId(id)
    }
}