package com.example.flatline.features.archivedlistings.datasource

import com.example.flatline.features.archivedlistings.datasource.mappers.toArchivedListings
import com.example.flatline.features.archivedlistings.datasource.network.ArchivedListingsApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit

class ArchivedListingsRepository(private val retrofit: Retrofit) {
    val api: ArchivedListingsApi
        get() = retrofit.create(ArchivedListingsApi::class.java)

    suspend fun getArchivedListings() = withContext(Dispatchers.IO) {
        api.getArchivedListings().toArchivedListings()
    }

    suspend fun unhideListingWithId(id: Int) = withContext(Dispatchers.IO) {
        api.unhideListingWithId(id)
    }
}
