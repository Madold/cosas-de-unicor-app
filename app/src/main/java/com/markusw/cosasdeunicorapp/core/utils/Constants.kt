package com.markusw.cosasdeunicorapp.core.utils

import com.markusw.cosasdeunicorapp.BuildConfig


object Constants {
    const val TEST_BANNER_AD_ID = "ca-app-pub-3940256099942544/6300978111"
    const val TEST_INTERSTITIAL_AD_ID = "ca-app-pub-3940256099942544/1033173712"
    const val PUSH_NOTIFICATION_SERVER_KEY = "AAAAqGR5COg:APA91bF46kuuhiW1V4xBk5wUbaFy-bHCz1bgYh5uKevr2pqdszORCWglU1hYdoNhZXXLQIYSDjCIdglPGRhhnC3ylcW2eoQEsop1f1-whPye4F-G8eH59shKWQs8N0DArukv2M-KiDnU"
    const val PUSH_NOTIFICATION_API_BASE_URL = "https://fcm.googleapis.com/"
    const val PUSH_NOTIFICATION_CHANNEL_ID = "push_notifications"
    const val PUSH_NOTIFICATION_CHANNEL_NAME = "Push Notifications Channel"
    const val PUSH_NOTIFICATION_CHANNEL_DESCRIPTION = "Channel for push notifications"

    fun provideAdUnitId(): String {

        if (BuildConfig.BUILD_TYPE == "release") {

            return "ca-app-pub-7843708863859210/6794540184"
        }

        return TEST_BANNER_AD_ID
    }
}