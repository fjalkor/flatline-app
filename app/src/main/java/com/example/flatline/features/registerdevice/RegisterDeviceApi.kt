package com.example.flatline.features.registerdevice

import com.example.flatline.features.registerdevice.remote.model.RegisterDeviceRequestBody
import com.example.flatline.features.registerdevice.remote.model.RegisterDeviceResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface RegisterDeviceApi {
    @POST("register")
    suspend fun registerDevice(@Body body: RegisterDeviceRequestBody): RegisterDeviceResponse
}