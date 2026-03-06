package com.example.flatline.features.notifications

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.flatline.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MessagingService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // TODO: implement my own logger
        Log.d("FCM", "Token: $token")
        // TODO: send to backend
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        remoteMessage.notification?.let {
            Log.d("FCM", "received message: ${it.title}, ${it.body}")
            showNotification(it.title, it.body)
        }
    }

    fun showNotification(title: String?, body: String?) {
        val builder = NotificationCompat.Builder(this, "fcm_channel")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title ?: "Notification")
            .setContentText(body ?: "")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
            == PackageManager.PERMISSION_GRANTED
        ) {
            NotificationManagerCompat.from(this).notify(1, builder.build())
        } else {
            Log.d("FCM", "permission denied")
        }
    }
}