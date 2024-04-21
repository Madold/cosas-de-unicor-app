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
import com.markusw.cosasdeunicorapp.teacher_rating.domain.model.Review
import com.markusw.cosasdeunicorapp.teacher_rating.presentation.TeacherRatingEvent

@Composable
fun ReviewsList(
    reviews: List<Review>,
    onEvent: (TeacherRatingEvent) -> Unit,
    modifier: Modifier = Modifier
) {

    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(reviews) { review ->

            val animatable = remember {
                Animatable(0.5f)
            }
            LaunchedEffect(key1 = true) {
                animatable.animateTo(1f, tween(350, easing = FastOutSlowInEasing))
            }

            ReviewItem(
                review = review,
                onEvent = onEvent,
                modifier = Modifier.graphicsLayer {
                    this.scaleX = animatable.value
                    this.scaleY = animatable.value
                }
            )
        }
    }

}