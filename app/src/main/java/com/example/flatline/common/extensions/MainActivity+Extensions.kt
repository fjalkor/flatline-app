package com.example.flatline.common.extensions

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.widget.Toast
import com.example.flatline.MainActivity

fun MainActivity.notifyUser(text: String, duration: Int = Toast.LENGTH_LONG) =
    Toast.makeText(this, text, duration).show()

fun MainActivity.registerNotificationChannel() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            "fcm_channel",
            "FCM Notifications",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Firebase push notifications"
        }

        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
    }
}
