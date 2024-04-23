@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)

package com.markusw.cosasdeunicorapp.teacher_rating.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
import com.markusw.cosasdeunicorapp.core.presentation.Button
import com.markusw.cosasdeunicorapp.teacher_rating.domain.model.TeacherRating
import com.markusw.cosasdeunicorapp.teacher_rating.domain.model.TeacherRating.Homie
import com.markusw.cosasdeunicorapp.teacher_rating.domain.model.TeacherRating.Pushy
import com.markusw.cosasdeunicorapp.teacher_rating.domain.model.TeacherRating.Ruthless
import com.markusw.cosasdeunicorapp.teacher_rating.domain.model.TeacherRating.Supportive
import com.markusw.cosasdeunicorapp.teacher_rating.presentation.composables.RATING_BAR_LABEL_WEIGHT
import com.markusw.cosasdeunicorapp.teacher_rating.presentation.composables.RatingChip
import com.markusw.cosasdeunicorapp.teacher_rating.presentation.composables.ReviewsList
import com.markusw.cosasdeunicorapp.teacher_rating.presentation.composables.TeacherRatingBar
import com.markusw.cosasdeunicorapp.ui.theme.homie_color
import com.markusw.cosasdeunicorapp.ui.theme.pushy_color
import com.markusw.cosasdeunicorapp.ui.theme.ruthless_color
import com.markusw.cosasdeunicorapp.ui.theme.supportive_color


@Composable
fun TeacherDetailsScreen(
    state: TeacherRatingState,
    onEvent: (TeacherRatingEvent) -> Unit,
    mainNavController: NavController
) {
    val teacher = state.selectedTeacher
    val isReviewsListEmpty = teacher.reviews.isEmpty()
    val totalReviews = remember(teacher) {
        teacher.reviews.size
    }
    val homieReviewsCount = remember(teacher) {
        teacher.reviews.count { it.vote == Homie }
    }
    val pushyReviewsCount = remember(teacher) {
        teacher.reviews.count { it.vote == Pushy }
    }
    val supportiveReviewsCount = remember(teacher) {
        teacher.reviews.count { it.vote == Supportive }
    }
    val ruthlessReviewsCount = remember(teacher) {
        teacher.reviews.count { it.vote == Ruthless }
    }
    val sheetState = rememberModalBottomSheetState()
    var isBottomSheetVisible by remember {
        mutableStateOf(false)
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = {
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
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    isBottomSheetVisible = true
                },
                shape = CircleShape
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_pencil),
                    contentDescription = null,
                    tint = Color.White
                )
            }
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

            Text(text = "Comentarios (${totalReviews})")
            Divider()

            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                if (isReviewsListEmpty) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.empty_comments_ilustration),
                            contentDescription = null,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Text(
                            text = "No hay reseñas ¡Realiza la primera!",
                        )
                    }
                } else {
                    ReviewsList(
                        state = state,
                        onEvent = onEvent,
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.TopCenter)
                    )
                }
            }

            if (isBottomSheetVisible) {
                ModalBottomSheet(
                    onDismissRequest = { isBottomSheetVisible = false },
                    sheetState = sheetState
                ) {
                    SheetContent(
                        state = state,
                        onEvent = onEvent
                    )
                }
            }

        }
    }
}

@Composable
private fun SheetContent(
    state: TeacherRatingState,
    onEvent: (TeacherRatingEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.navigationBars)
            .padding(horizontal = 16.dp)
        ,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Hacer reseña",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Text(text = "Califica al docente")

        FlowRow(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            TeacherRating.entries.forEach { rating ->
                when (rating) {
                    Homie -> {
                        RatingChip(
                            onClick = {
                                onEvent(
                                    TeacherRatingEvent.ChangeTeacherRating(
                                        rating
                                    )
                                )
                            },
                            label = {
                                Text(text = "Valecita")
                            },
                            backgroundColor = homie_color,
                            selected = state.selectedTeacherRating == rating
                        )
                    }

                    Ruthless -> {
                        RatingChip(
                            onClick = {
                                onEvent(
                                    TeacherRatingEvent.ChangeTeacherRating(
                                        rating
                                    )
                                )
                            },
                            label = {
                                Text(text = "Cuchilla")
                            },
                            backgroundColor = ruthless_color,
                            selected = state.selectedTeacherRating == rating
                        )
                    }

                    Pushy -> {
                        RatingChip(
                            onClick = {
                                onEvent(
                                    TeacherRatingEvent.ChangeTeacherRating(
                                        rating
                                    )
                                )
                            },
                            label = {
                                Text(text = "Pesao")
                            },
                            backgroundColor = pushy_color,
                            selected = state.selectedTeacherRating == rating
                        )
                    }

                    Supportive -> {
                        RatingChip(
                            onClick = {
                                onEvent(
                                    TeacherRatingEvent.ChangeTeacherRating(
                                        rating
                                    )
                                )
                            },
                            label = {
                                Text(text = "Calidoso")
                            },
                            backgroundColor = supportive_color,
                            selected = state.selectedTeacherRating == rating
                        )
                    }
                }
            }
        }

        OutlinedTextField(
            value = state.userOpinion,
            onValueChange = { onEvent(TeacherRatingEvent.ChangeUserOpinion(it)) },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text(text = "Tu opinión (opcional)")
            }
        )


        Button(
            onClick = { onEvent(TeacherRatingEvent.SubmitRating) },
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.End)
        ) {
            Text("Relizar reseña")
        }


    }
}