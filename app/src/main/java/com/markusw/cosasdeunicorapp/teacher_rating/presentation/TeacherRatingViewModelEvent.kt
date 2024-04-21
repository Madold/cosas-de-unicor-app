package com.markusw.cosasdeunicorapp.teacher_rating.presentation

sealed interface TeacherRatingViewModelEvent {
    data object ReviewSavedSuccessfully : TeacherRatingViewModelEvent
}