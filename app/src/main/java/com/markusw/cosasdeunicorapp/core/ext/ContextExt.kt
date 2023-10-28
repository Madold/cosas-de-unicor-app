package com.markusw.cosasdeunicorapp.core.ext

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.markusw.cosasdeunicorapp.core.utils.Constants.INTERSTITIAL_AD_ID

/**
 * Shows a toast message.
 * @param message The message to show.
 * @param duration The duration of the toast.
 * @see Toast
 * @see Toast.LENGTH_SHORT
 * @see Toast.LENGTH_LONG
 * @see Toast.makeText
 * @see Toast.show
 * @see Context
 */
fun Context.toast(message: String, duration: Int= Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

/**
 * Opens the app settings screen.
 */
fun Context.openAppSettings() {
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", this.packageName, this::class.java.simpleName)
    ).also(::startActivity)
}

/**
 * Finds the activity of the context.
 * @return The activity of the context.
 */
fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

/**
 * Shows an interstitial ad.
 * @param onAdLoaded The action to perform when the ad is loaded.
 * @param onAdFailedToLoad The action to perform when the ad failed to load.
 * @param onAdDismissed The action to perform when the ad is dismissed.
 * @param onAdFailedToShowFullScreen The action to perform when the ad failed to show.
 */
fun Context.showInterstitialAd(
    onAdLoaded: () -> Unit = {},
    onAdFailedToLoad: (LoadAdError) -> Unit = {},
    onAdDismissed: () -> Unit = {},
    onAdFailedToShowFullScreen: () -> Unit = {}
) {

    val adRequest = AdRequest.Builder().build()

    InterstitialAd.load(
        this,
        INTERSTITIAL_AD_ID,
        adRequest,
        object : InterstitialAdLoadCallback() {
            override fun onAdLoaded(ad: InterstitialAd) {
                super.onAdLoaded(ad)
                onAdLoaded()

                ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        super.onAdDismissedFullScreenContent()
                        onAdDismissed()
                    }

                    override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                        super.onAdFailedToShowFullScreenContent(p0)
                        onAdFailedToShowFullScreen()
                    }
                }

                findActivity()?.let { activity ->
                    ad.show(activity)
                } ?: run {
                    onAdFailedToShowFullScreen()
                }

            }

            override fun onAdFailedToLoad(error: LoadAdError) {
                super.onAdFailedToLoad(error)
                onAdFailedToLoad(error)
            }
        }
    )

}

/**
 * Checks if a permission is granted.
 * @param permission The permission to check.
 * @return True if the permission is granted, false otherwise.
 */
fun Context.isPermissionGranted(permission: String): Boolean {
    return checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
}