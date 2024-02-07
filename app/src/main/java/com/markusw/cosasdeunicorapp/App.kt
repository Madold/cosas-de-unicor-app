package com.markusw.cosasdeunicorapp

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import com.google.android.gms.ads.MobileAds
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import com.markusw.cosasdeunicorapp.core.utils.Constants.PUSH_NOTIFICATION_CHANNEL_DESCRIPTION
import com.markusw.cosasdeunicorapp.core.utils.Constants.PUSH_NOTIFICATION_CHANNEL_ID
import com.markusw.cosasdeunicorapp.core.utils.Constants.PUSH_NOTIFICATION_CHANNEL_NAME
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class App : Application() {


    override fun onCreate() {
        super.onCreate()

        MobileAds.initialize(this)

        if (BuildConfig.DEBUG) {
            setupTimber()
            disableFirebaseDataCollection()
        }

        createNotificationChannels()
    }

    /**
     * Creates the notification channels for the app
     */
    private fun createNotificationChannels() {
       createPushNotificationChannel()
    }

    /**
     * Creates the push notification channel
     */
    private fun createPushNotificationChannel() {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val pushNotificationChannel = NotificationChannel(
            PUSH_NOTIFICATION_CHANNEL_ID,
            PUSH_NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = PUSH_NOTIFICATION_CHANNEL_DESCRIPTION
            enableLights(true)
            enableVibration(true)
            lightColor = Color.YELLOW
        }

        notificationManager.createNotificationChannel(pushNotificationChannel)
    }

    private fun disableFirebaseDataCollection() {
        Firebase.analytics.setAnalyticsCollectionEnabled(false)
        Firebase.crashlytics.setCrashlyticsCollectionEnabled(false)
    }

    private fun setupTimber() {
        val formatStrategy = PrettyFormatStrategy
            .newBuilder()
            .showThreadInfo(true)
            .methodOffset(5)
            .tag("")
            .build()
        Logger.addLogAdapter(AndroidLogAdapter(formatStrategy))
        Timber.plant(object : Timber.DebugTree() {
            override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
                Logger.log(priority, "-$tag", message, t)
            }
        })

        Timber.d("Starting App...")
    }
}