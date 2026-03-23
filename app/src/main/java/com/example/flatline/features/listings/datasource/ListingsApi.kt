package com.example.flatline.features.listings.datasource

import com.example.flatline.features.listings.datasource.model.ListingsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ListingsApi {
    @GET("listings")
    suspend fun getListings(): ListingsResponse

    @POST("hide-listing")
    suspend fun hideListingWithId(@Query("id") id: Int): Response<Unit>
}