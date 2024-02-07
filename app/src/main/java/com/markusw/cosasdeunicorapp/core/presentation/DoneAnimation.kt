package com.markusw.cosasdeunicorapp.core.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.markusw.cosasdeunicorapp.R

@Composable
fun DoneAnimation() {

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.done_animation))
    val progress by animateLottieCompositionAsState(composition)

    LottieAnimation(
        composition = composition,
        progress = { progress }
    )

}