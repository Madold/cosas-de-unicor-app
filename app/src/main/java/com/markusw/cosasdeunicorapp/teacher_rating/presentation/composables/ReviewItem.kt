package com.markusw.cosasdeunicorapp.teacher_rating.presentation.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.markusw.cosasdeunicorapp.R
import com.markusw.cosasdeunicorapp.core.domain.model.User
import com.markusw.cosasdeunicorapp.core.presentation.ProfileAvatar
import com.markusw.cosasdeunicorapp.core.utils.TextUtils
import com.markusw.cosasdeunicorapp.teacher_rating.domain.model.Review
import com.markusw.cosasdeunicorapp.teacher_rating.domain.model.TeacherRating
import com.markusw.cosasdeunicorapp.teacher_rating.presentation.TeacherRatingEvent
import com.markusw.cosasdeunicorapp.ui.theme.homie_color
import com.markusw.cosasdeunicorapp.ui.theme.md_theme_light_primary
import com.markusw.cosasdeunicorapp.ui.theme.pushy_color
import com.markusw.cosasdeunicorapp.ui.theme.ruthless_color
import com.markusw.cosasdeunicorapp.ui.theme.supportive_color

@Composable
fun ReviewItem(
    review: Review,
    teacherId: String,
    onEvent: (TeacherRatingEvent) -> Unit,
    loggedUser: User,
    modifier: Modifier = Modifier,
    isFromUser: Boolean = false,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ReviewHeader(
            review = review,
            isFromUser = isFromUser,
            onEvent = onEvent
        )
        Text(text = review.content)
        ReviewFooter(
            review = review,
            onEvent = onEvent,
            teacherId = teacherId,
            loggedUser = loggedUser
        )
    }
    Divider(
        color = if (isFromUser) md_theme_light_primary else DividerDefaults.color,
    )

}

@Composable
private fun ReviewFooter(
    review: Review,
    teacherId: String,
    onEvent: (TeacherRatingEvent) -> Unit,
    loggedUser: User
) {

    val isReviewLiked = remember (review.likes) {
        review.likes.contains(loggedUser.uid)
    }
    val isReviewDisliked = remember (review.dislikes) {
        review.dislikes.contains(loggedUser.uid)
    }
    val likeIcon = if (isReviewLiked) R.drawable.ic_like_filled else R.drawable.ic_like
    val dislikeIcon = if (isReviewDisliked) R.drawable.ic_dislike_filled else R.drawable.ic_dislike

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
            IconButton(onClick = {
                onEvent(TeacherRatingEvent.ToggleReviewLike(teacherId, review.author.uid))
            }) {
                Icon(
                    painter = painterResource(id = likeIcon),
                    contentDescription = null,
                    tint = if (isReviewLiked) MaterialTheme.colorScheme.primary else LocalContentColor.current
                )
            }

            Text(text = review.dislikes.count().toString())
            IconButton(onClick = {
                onEvent(TeacherRatingEvent.ToggleReviewDislike(teacherId, review.author.uid))
            }) {
                Icon(
                    painter = painterResource(id = dislikeIcon),
                    contentDescription = null,
                    tint = if (isReviewDisliked) MaterialTheme.colorScheme.primary else LocalContentColor.current
                )
            }
        }
    }
}

@Composable
private fun ReviewHeader(
    review: Review,
    isFromUser: Boolean = false,
    onEvent: (TeacherRatingEvent) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        AuthorInfo(review = review)
        Column(
            horizontalAlignment = Alignment.End
        ) {
            Text(text = TextUtils.formatReviewTimestamp(review.timestamp))
            if (isFromUser) {
                OutlinedButton(onClick = { onEvent(TeacherRatingEvent.DeleteReview(review)) }) {
                    Text(text = "Eliminar")
                }
            }
        }
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