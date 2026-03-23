package com.example.flatline

import androidx.compose.runtime.mutableStateOf

object Config {
    val environment = mutableStateOf(Environment.PROD)
}

enum class Environment(val baseUrl: String) {
    LOCAL("http://10.0.2.2:8081"),
    PROD("https://flatline.ibrandes.com"),
}