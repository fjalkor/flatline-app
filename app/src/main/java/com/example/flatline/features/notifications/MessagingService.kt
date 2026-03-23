package com.example.flatline.features.notifications

import android.Manifest
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.example.flatline.R
import com.example.flatline.common.datastore.Store
import com.example.flatline.features.registerdevice.RegisterDeviceApi
import com.example.flatline.features.registerdevice.remote.model.RegisterDeviceRequestBody
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import retrofit2.Retrofit

class MessagingService : FirebaseMessagingService() {
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val retrofit: Retrofit by inject()
    private val store: Store by inject()
    private val registerDeviceApi: RegisterDeviceApi
        get() = retrofit.create(RegisterDeviceApi::class.java)

    private val currentToken by lazy {
        store
            .getFcmToken()
            .stateIn(
                scope = serviceScope,
                started = SharingStarted.Lazily,
                initialValue = "",
            )
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // TODO: implement my own logger
        Log.d("FCM", "Token: $token")
        if (currentToken.value.isEmpty()) {
            Log.e("FCM", "current token is empty - initial load?")
        }

        if (token != currentToken.value) {
            serviceScope.launch {
                store.setFcmToken(token)
                runCatching {
                    registerDeviceApi.registerDevice(
                        RegisterDeviceRequestBody(
                            token,
                            "and"
                        )
                    )
                }
                    .onSuccess { Log.d("FCM", "device registered successfully: $token") }
                    .onFailure { Log.w("FCM", "failed to register service: $it") }
            }
        }
    }

    //adb shell am start -a android.intent.action.VIEW \
    //-d "https://example.com/product/123" \
    //com.example.app

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        val title = remoteMessage.data["title"]
        val body = remoteMessage.data["body"]
        val url = remoteMessage.data["url"]

        Log.d("FCM", "received message: $title, $body, $url")
        showNotification(title, body, url)
    }

    fun showNotification(title: String?, body: String?, url: String?) {
        val intent = Intent(Intent.ACTION_VIEW, url?.toUri()).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, "fcm_channel_v2")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title ?: "Notification")
            .setContentText(body ?: "")
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
            == PackageManager.PERMISSION_GRANTED
        ) {
            NotificationManagerCompat.from(this)
                .notify(System.currentTimeMillis().toInt(), notification)
        } else {
            Log.d("FCM", "permission denied")
        }
    }
}