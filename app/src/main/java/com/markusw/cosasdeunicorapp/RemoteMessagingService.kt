package com.markusw.cosasdeunicorapp

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.markusw.cosasdeunicorapp.core.utils.Constants.PUSH_NOTIFICATION_CHANNEL_ID

/**
 * Created by Markus on 4-12-2023.
 * Service to receive push notifications
 */
@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class RemoteMessagingService : FirebaseMessagingService() {

    private val notificationManager by lazy { getSystemService(NotificationManager::class.java) }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val notificationTitle = message.data["title"] ?: return
        val notificationBody = message.data["message"] ?: return
        val isBuildVersionGreaterThanTiramisu = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU

        if (isBuildVersionGreaterThanTiramisu && !isPostNotificationPermissionGranted()) {
            return
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, PUSH_NOTIFICATION_CHANNEL_ID)
            .setContentTitle(notificationTitle)
            .setContentText(notificationBody)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(0, notification)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun isPostNotificationPermissionGranted(): Boolean {
        return checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
    }

}