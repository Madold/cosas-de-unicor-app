package com.markusw.cosasdeunicorapp.teacher_rating.presentation.composables

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.markusw.cosasdeunicorapp.teacher_rating.presentation.TeacherRatingEvent
import com.markusw.cosasdeunicorapp.teacher_rating.presentation.TeacherRatingState

@Composable
fun ReviewsList(
    state: TeacherRatingState,
    onEvent: (TeacherRatingEvent) -> Unit,
    modifier: Modifier = Modifier
) {

    val userReview = remember(state.selectedTeacher.reviews) {
        state.selectedTeacher.reviews.find { it.author.uid == state.loggedUser.uid }
    }
    val otherReviews = remember(state.selectedTeacher.reviews) {
        state.selectedTeacher.reviews.filter { it.author.uid != state.loggedUser.uid }
    }

    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        userReview?.let {
            item {

                val animatable = remember {
                    Animatable(0.5f)
                }
                LaunchedEffect(key1 = true) {
                    animatable.animateTo(1f, tween(350, easing = FastOutSlowInEasing))
                }

                Text(
                    text = "Tu opinión",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.fillMaxWidth()
                )
                ReviewItem(
                    review = it,
                    onEvent = onEvent,
                    modifier = Modifier.graphicsLayer {
                        this.scaleX = animatable.value
                        this.scaleY = animatable.value
                    },
                    isFromUser = true,
                    teacherId = state.selectedTeacher.id,
                    loggedUser = state.loggedUser
                )
            }
        }

        items(otherReviews) { review ->

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
                },
                isFromUser = false,
                teacherId = state.selectedTeacher.id,
                loggedUser = state.loggedUser
            )
        }
    }

}