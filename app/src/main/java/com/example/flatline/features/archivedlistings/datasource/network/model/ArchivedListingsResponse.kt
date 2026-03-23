package com.example.flatline.features.archivedlistings.datasource.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class ArchivedListingsResponse(
    @SerialName("items")
    val items: List<ArchivedListingRemote>? = null,
)
