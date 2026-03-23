package com.example.flatline.features.listings.datasource.extensions

import com.example.flatline.features.listings.datasource.model.ListingsResponse
import com.example.flatline.features.listings.domain.Listings

fun ListingsResponse.toListings() =
    Listings(
        items = items?.mapNotNull { it.toListing() }.orEmpty(),
        lastUpdatedAt = lastUpdatedAt,
    )