package com.markusw.cosasdeunicorapp.teacher_rating.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.markusw.cosasdeunicorapp.R
import com.markusw.cosasdeunicorapp.core.ext.pop
import com.markusw.cosasdeunicorapp.core.presentation.AppTopBar
import com.markusw.cosasdeunicorapp.teacher_rating.domain.model.TeacherRating
import com.markusw.cosasdeunicorapp.teacher_rating.presentation.composables.RATING_BAR_LABEL_WEIGHT
import com.markusw.cosasdeunicorapp.teacher_rating.presentation.composables.TeacherRatingBar
import timber.log.Timber


@Composable
fun TeacherDetailsScreen(
    state: TeacherRatingState,
    onEvent: (TeacherRatingEvent) -> Unit,
    mainNavController: NavController
) {
    val teacher = state.selectedTeacher
    val totalReviews = remember(teacher) {
        teacher.reviews.size
    }
    val homieReviewsCount = remember(teacher) {
        teacher.reviews.count { it.vote == TeacherRating.Homie }
    }
    val pushyReviewsCount = remember(teacher) {
        teacher.reviews.count { it.vote == TeacherRating.Pushy }
    }
    val supportiveReviewsCount = remember(teacher) {
        teacher.reviews.count { it.vote == TeacherRating.Supportive }
    }
    val ruthlessReviewsCount = remember(teacher) {
        teacher.reviews.count { it.vote == TeacherRating.Ruthless }
    }

    Scaffold(
        topBar = {
            AppTopBar(title = {
                Text(
                    text = "Reseñas", color = Color.White,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            },
                navigationIcon = {
                    IconButton(onClick = { mainNavController.pop() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_left),
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = state.selectedTeacher.teacherName,
                modifier = Modifier.fillMaxWidth(),
                color = Color.White,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                textAlign = TextAlign.Center
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                TeacherRatingBar(
                    label = {
                        Text(
                            text = "Cuchilla",
                            color = Color.White,
                            modifier = Modifier.weight(RATING_BAR_LABEL_WEIGHT)
                        )
                    },
                    percentage = if (totalReviews == 0) 0f else ruthlessReviewsCount.toFloat() / totalReviews,
                    count = ruthlessReviewsCount,
                )

                TeacherRatingBar(
                    label = {
                        Text(
                            text = "Pesao",
                            color = Color.White,
                            modifier = Modifier.weight(RATING_BAR_LABEL_WEIGHT)
                        )
                    },
                    percentage = if (totalReviews == 0) 0f else pushyReviewsCount.toFloat() / totalReviews,
                    count = pushyReviewsCount,
                )

                TeacherRatingBar(
                    label = {
                        Text(
                            text = "Calidoso",
                            color = Color.White,
                            modifier = Modifier.weight(RATING_BAR_LABEL_WEIGHT)
                        )
                    },
                    percentage = if (totalReviews == 0) 0f else supportiveReviewsCount.toFloat() / totalReviews,
                    count = supportiveReviewsCount,
                )

                TeacherRatingBar(
                    label = {
                        Text(
                            text = "Valecita",
                            color = Color.White,
                            modifier = Modifier.weight(RATING_BAR_LABEL_WEIGHT)
                        )
                    },
                    percentage = if (totalReviews == 0) 0f else homieReviewsCount.toFloat() / totalReviews,
                    count = homieReviewsCount,
                )
            }
        }
    }

}