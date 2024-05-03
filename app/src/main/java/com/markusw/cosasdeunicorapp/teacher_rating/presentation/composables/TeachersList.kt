package com.markusw.cosasdeunicorapp.teacher_rating.presentation.composables

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.markusw.cosasdeunicorapp.teacher_rating.presentation.TeacherRatingEvent
import com.markusw.cosasdeunicorapp.teacher_rating.presentation.TeacherRatingState

@Composable
fun TeachersList(
    state: TeacherRatingState,
    onEvent: (TeacherRatingEvent) -> Unit,
    modifier: Modifier = Modifier,
    mainNavController: NavController
) {

    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(state.teachers) { teacherReview ->

            val animatable = remember {
                Animatable(0.5f)
            }

            LaunchedEffect(true) {
                animatable.animateTo(1f, tween(350, easing = FastOutSlowInEasing))
            }

            TeacherCard(
                teacher = teacherReview,
                onEvent = onEvent,
                mainNavController = mainNavController,
                modifier = Modifier
                    .graphicsLayer {
                        this.scaleX = animatable.value
                        this.scaleY = animatable.value
                    }
            )
        }
    }

}