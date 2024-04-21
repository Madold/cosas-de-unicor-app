package com.markusw.cosasdeunicorapp.teacher_rating.presentation.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.markusw.cosasdeunicorapp.R
import com.markusw.cosasdeunicorapp.core.presentation.ProfileAvatar
import com.markusw.cosasdeunicorapp.core.utils.TextUtils
import com.markusw.cosasdeunicorapp.teacher_rating.domain.model.Review
import com.markusw.cosasdeunicorapp.teacher_rating.domain.model.TeacherRating
import com.markusw.cosasdeunicorapp.teacher_rating.presentation.TeacherRatingEvent
import com.markusw.cosasdeunicorapp.ui.theme.homie_color
import com.markusw.cosasdeunicorapp.ui.theme.pushy_color
import com.markusw.cosasdeunicorapp.ui.theme.ruthless_color
import com.markusw.cosasdeunicorapp.ui.theme.supportive_color

@Composable
fun ReviewItem(
    review: Review,
    onEvent: (TeacherRatingEvent) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ReviewHeader(review = review)
        Text(text = review.content)
        ReviewFooter(review = review, onEvent = onEvent)
    }
    Divider()

}

@Composable
private fun ReviewFooter(
    review: Review,
    onEvent: (TeacherRatingEvent) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "¿Fué util esta reseña?")

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = review.likes.count().toString())
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_like),
                    contentDescription = null
                )
            }

            Text(text = review.dislikes.count().toString())
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_dislike),
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
private fun ReviewHeader(
    review: Review
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        AuthorInfo(review = review)
        Text(text = TextUtils.formatReviewTimestamp(review.timestamp))
    }
}

@Composable
private fun AuthorInfo(
    review: Review
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ProfileAvatar(imageUrl = review.author.photoUrl, size = 42.dp)
        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(text = review.author.displayName)
            when (review.vote) {
                TeacherRating.Homie -> {
                    RatingChip(
                        onClick = {},
                        label = {
                            Text(text = "Valecita")
                        },
                        backgroundColor = homie_color,
                        selected = true
                    )
                }

                TeacherRating.Ruthless -> {
                    RatingChip(
                        onClick = {},
                        label = {
                            Text(text = "Cuchilla")
                        },
                        backgroundColor = ruthless_color,
                        selected = true
                    )
                }

                TeacherRating.Pushy -> {
                    RatingChip(
                        onClick = {},
                        label = {
                            Text(text = "Pesao")
                        },
                        backgroundColor = pushy_color,
                        selected = true
                    )
                }

                TeacherRating.Supportive -> {
                    RatingChip(
                        onClick = {},
                        label = {
                            Text(text = "Calidoso")
                        },
                        backgroundColor = supportive_color,
                        selected = true
                    )
                }
            }
        }
    }
}