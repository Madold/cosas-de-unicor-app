package com.markusw.cosasdeunicorapp.core.utils

import kotlin.random.Random

/**
 * Created by Markus on 29-10-2023.
 */
object AdUtils {

    /**
     * Returns true if an interstitial ad should be shown.
     */
    fun shouldShowInterstitialAd(): Boolean {
        val randomNumber = Random.nextDouble(0.0, 1.0)
        val threshold = 0.175

        return randomNumber < threshold
    }

}