package com.markusw.cosasdeunicorapp.teacher_rating.presentation.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.markusw.cosasdeunicorapp.R
import com.markusw.cosasdeunicorapp.core.domain.model.User
import com.markusw.cosasdeunicorapp.core.presentation.Screens
import com.markusw.cosasdeunicorapp.teacher_rating.domain.model.Review
import com.markusw.cosasdeunicorapp.teacher_rating.domain.model.TeacherRating
import com.markusw.cosasdeunicorapp.teacher_rating.domain.model.TeacherReview
import com.markusw.cosasdeunicorapp.teacher_rating.presentation.TeacherRatingEvent
import com.markusw.cosasdeunicorapp.ui.theme.CosasDeUnicorAppTheme
import com.markusw.cosasdeunicorapp.ui.theme.home_bottom_bar_background
import com.markusw.cosasdeunicorapp.ui.theme.homie_color
import com.markusw.cosasdeunicorapp.ui.theme.pushy_color
import com.markusw.cosasdeunicorapp.ui.theme.ruthless_color
import com.markusw.cosasdeunicorapp.ui.theme.supportive_color

@Composable
fun TeacherCard(
    teacher: TeacherReview,
    onEvent: (TeacherRatingEvent) -> Unit,
    mainNavController: NavController,
    modifier: Modifier = Modifier
) {

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
    val maxRating = remember(teacher) {
        listOf(
            homieReviewsCount,
            pushyReviewsCount,
            supportiveReviewsCount,
            ruthlessReviewsCount
        ).maxOrNull() ?: 0
    }
    val markerColor = remember(teacher) {
        when (maxRating) {
            homieReviewsCount -> homie_color
            pushyReviewsCount -> pushy_color
            supportiveReviewsCount -> supportive_color
            ruthlessReviewsCount -> ruthless_color
            else -> Color.White
        }
    }

    Spacer(modifier = Modifier.height(8.dp))
    Box(
        modifier = modifier
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = home_bottom_bar_background
            ),
            shape = RoundedCornerShape(20.dp),
        ) {
            Column(
                modifier = Modifier
                    .clickable {
                        onEvent(TeacherRatingEvent.ChangeSelectedTeacher(teacher))
                        mainNavController.navigate(Screens.TeacherRatingDetail.route)
                    }
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = teacher.teacherName,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.White,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
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
                        isInsideCard = true
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
                        isInsideCard = true
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
                        isInsideCard = true
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
                        isInsideCard = true
                    )

                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_comment),
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                }
            }
        }

        Icon(
            painter = painterResource(id = R.drawable.ic_saved),
            contentDescription = null,
            tint = markerColor,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding( end = 8.dp)
                .size(32.dp)
        )
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun TeacherCardPreview() {

    val exampleTeacher = TeacherReview(
        teacherName = "Ñengele",
        id = "1",
        reviews = listOf(
            Review(
                content = "Muy buen profesor, explica muy bien",
                timestamp = 2324344,
                vote = TeacherRating.Homie,
                author = User(
                    uid = "1",
                    displayName = "Markus",
                    email = "m@gmail.com",
                    photoUrl = "https://www.google.com"
                ),
                likes = emptyList(),
                dislikes = emptyList()
            ),
            Review(
                content = "Mal profesor, no explica bien",
                timestamp = 2324344,
                vote = TeacherRating.Pushy,
                author = User(
                    uid = "2",
                    displayName = "Markus",
                    email = "asasasasfsdf",
                    photoUrl = "https://www.google.com"
                ),
                likes = emptyList(),
                dislikes = emptyList()
            ),
            Review(
                content = "Muy buen profesor, explica muy bien",
                timestamp = 2324344,
                vote = TeacherRating.Homie,
                author = User(
                    uid = "3",
                    displayName = "Markus",
                    email = "asasasasfsdf",
                    photoUrl = "https://www.google.com"
                ),
                likes = emptyList(),
                dislikes = emptyList()
            ),
            Review(
                content = "Muy buen profesor, explica muy bien",
                timestamp = 2324344,
                vote = TeacherRating.Homie,
                author = User(
                    uid = "4",
                    displayName = "Markus",
                    email = "asasasasfsdf",
                    photoUrl = "https://www.google.com"
                ),
                likes = emptyList(),
                dislikes = emptyList()
            ),

            )
    )

    CosasDeUnicorAppTheme {
        TeacherCard(
            teacher = exampleTeacher,
            onEvent = {},
            mainNavController = rememberNavController()
        )
    }
}