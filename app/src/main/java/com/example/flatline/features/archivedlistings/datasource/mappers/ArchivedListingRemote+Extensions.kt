package com.example.flatline.features.archivedlistings.datasource.mappers

import com.example.flatline.common.extensions.utf8Encoded
import com.example.flatline.features.archivedlistings.datasource.network.model.ArchivedListingRemote
import com.example.flatline.features.archivedlistings.model.ArchivedListing

fun ArchivedListingRemote.toArchivedListing(): ArchivedListing? {
    if (id == null) return null
    if (title.isNullOrEmpty() || url.isNullOrEmpty() || description.isNullOrEmpty() || provider.isNullOrEmpty() || rent == null || area == null || rooms == null) return null

    if (address.isNullOrEmpty() && lat == null && lng == null) return null

    return ArchivedListing(
        id = id,
        title = title,
        rent = rent,
        area = area,
        rooms = rooms,
        wbs = wbs == true,
        lat = lat,
        lng = lng,
        address = address,
        url = url.utf8Encoded(),
        description = description,
        provider = provider,
    )
}