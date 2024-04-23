package com.markusw.cosasdeunicorapp.teacher_rating.presentation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.animation.with
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.markusw.cosasdeunicorapp.R
import com.markusw.cosasdeunicorapp.core.ext.pop
import com.markusw.cosasdeunicorapp.core.presentation.AppTopBar
import com.markusw.cosasdeunicorapp.teacher_rating.presentation.composables.TeachersList
import com.markusw.cosasdeunicorapp.ui.theme.CosasDeUnicorAppTheme

@Composable
fun TeacherRatingScreen(
    state: TeacherRatingState,
    onEvent: (TeacherRatingEvent) -> Unit,
    mainNavController: NavController
) {
    Scaffold(
        topBar = {
            AppTopBar(
                title = {
                    Text(
                        text = "Calificar docentes", color = Color.White,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { mainNavController.pop() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_left),
                            contentDescription = null
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_search),
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            AnimatedContent(
                targetState = state.isLoadingReviews,
                label = "loading_transition",
                transitionSpec = { fadeIn() togetherWith  fadeOut() }
            ) { isLoadingReviews ->
                if (isLoadingReviews) {
                    LoadingAnimation()
                } else {
                    TeachersList(
                        state = state,
                        onEvent = onEvent,
                        modifier = Modifier.fillMaxSize(),
                        mainNavController = mainNavController
                    )
                }

            }
        }
    }
}

@Preview
@Composable
fun TeacherRatingScreenPreview() {

    CosasDeUnicorAppTheme {
        TeacherRatingScreen(
            state = TeacherRatingState(),
            onEvent = {},
            mainNavController = rememberNavController()
        )
    }
}

@Composable
private fun LoadingAnimation() {

    val lottieComposition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.loading_animation))
    val progress by animateLottieCompositionAsState(
        composition = lottieComposition,
        iterations = LottieConstants.IterateForever
    )

    LottieAnimation(
        composition = lottieComposition,
        progress = { progress }
    )

}