package com.markusw.cosasdeunicorapp.core.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.markusw.cosasdeunicorapp.core.utils.Constants.ADS_ID

@Composable
fun AdmobBanner(
    modifier: Modifier = Modifier,
    onAdClick: () -> Unit = {},
    onAdFailedToLoad: (LoadAdError) -> Unit = { _ -> },
) {

    AndroidView(factory = { context ->
        AdView(context).apply {
            setAdSize(AdSize.BANNER)
            adUnitId = ADS_ID
            adListener = object: AdListener() {
                override fun onAdClicked() {
                    super.onAdClicked()
                    onAdClick()
                }

                override fun onAdFailedToLoad(p0: LoadAdError) {
                    super.onAdFailedToLoad(p0)
                    onAdFailedToLoad(p0)
                }
            }
            loadAd(AdRequest.Builder().build())
        }
    }, modifier = modifier)

}