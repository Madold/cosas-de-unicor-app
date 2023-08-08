package com.markusw.cosasdeunicorapp

import android.app.Application
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.installations.interop.BuildConfig
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App: Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Firebase.analytics.setAnalyticsCollectionEnabled(false)
        }
    }
}