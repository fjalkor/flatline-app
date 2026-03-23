package com.example.flatline.features.archivedlistings.datasource.mappers

import com.example.flatline.features.archivedlistings.datasource.network.model.ArchivedListingsResponse
import com.example.flatline.features.archivedlistings.model.ArchivedListings

fun ArchivedListingsResponse.toArchivedListings() =
    ArchivedListings(items = items?.mapNotNull { it.toArchivedListing() }.orEmpty())