package com.example.flatline.features.registerdevice.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegisterDeviceRequestBody(
    @SerialName("fcm_token")
    val token: String,
    @SerialName("platform")
    val platform: String,
)
