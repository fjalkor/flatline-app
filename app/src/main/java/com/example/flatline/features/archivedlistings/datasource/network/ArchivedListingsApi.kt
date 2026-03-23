package com.example.flatline.features.archivedlistings.datasource.network

import com.example.flatline.features.archivedlistings.datasource.network.model.ArchivedListingsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ArchivedListingsApi {
    @GET("archived-listings")
    suspend fun getArchivedListings(): ArchivedListingsResponse

    @POST("unhide-listing")
    suspend fun unhideListingWithId(@Query("id") id: Int): Response<Unit>
}