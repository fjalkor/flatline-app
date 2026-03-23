package com.example.flatline.features.listings.datasource.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ListingRemote(
    @SerialName("id")
    val id: Int? = null,
    @SerialName("title")
    val title: String? = null,
    @SerialName("rent")
    val rent: Double? = null,
    @SerialName("area")
    val area: Double? = null,
    @SerialName("rooms")
    val rooms: Int? = null,
    @SerialName("wbs")
    val wbs: Boolean? = null,
    @SerialName("lat")
    val lat: Double? = null,
    @SerialName("lng")
    val lng: Double? = null,
    @SerialName("address")
    val address: String? = null,
    @SerialName("url")
    val url: String? = null,
    @SerialName("description")
    val description: String? = null,
    @SerialName("provider")
    val provider: String? = null,
)
