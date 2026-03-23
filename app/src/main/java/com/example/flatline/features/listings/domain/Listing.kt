package com.example.flatline.features.listings.domain

data class Listing(
    val id: Int,
    val title: String,
    val rent: Double,
    val area: Double,
    val rooms: Int,
    val wbs: Boolean,
    val lat: Double? = null,
    val lng: Double? = null,
    val address: String? = null,
    val url: String,
    val description: String,
    val provider: String,
)
