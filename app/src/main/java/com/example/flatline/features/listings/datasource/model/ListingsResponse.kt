package com.example.flatline.features.listings.datasource.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ListingsResponse(
    @SerialName("items")
    val items: List<ListingRemote>? = null,
    @SerialName("last_updated_at")
    val lastUpdatedAt: String? = null,
)