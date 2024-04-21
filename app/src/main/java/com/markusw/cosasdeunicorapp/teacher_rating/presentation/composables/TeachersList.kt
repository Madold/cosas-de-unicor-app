package com.markusw.cosasdeunicorapp.teacher_rating.presentation.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
            TeacherCard(
                teacher = teacherReview,
                onEvent = onEvent,
                mainNavController = mainNavController
            )
        }
    }

}