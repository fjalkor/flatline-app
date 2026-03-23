package com.example.flatline

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.flatline.common.extensions.notifyUser
import com.example.flatline.common.extensions.registerNotificationChannel
import com.example.flatline.common.extensions.utf8Encoded
import com.example.flatline.ui.theme.FlatlineTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= 33) {
            requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1)
        } else {
            notifyUser("please open the app again and allow notifications.")
        }

        val targetUrl = intent.data?.getQueryParameter("url")

        val startDestination = if (targetUrl.isNullOrBlank()) {
            null
        } else {
            Routes.Details.route + "/" + targetUrl.utf8Encoded()
        }

        registerNotificationChannel()
        enableEdgeToEdge()
        setContent {
            FlatlineTheme {
                Host(startDestination = startDestination, onBack = { finish() })
            }
        }
    }
}
