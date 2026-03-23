package com.example.flatline.features.registerdevice.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegisterDeviceResponse(
    @SerialName("status_code")
    val statusCode: Int? = null,
)
