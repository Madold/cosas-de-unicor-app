package com.markusw.cosasdeunicorapp.teacher_rating.presentation

import com.markusw.cosasdeunicorapp.core.presentation.UiText

sealed interface TeacherRatingViewModelEvent {
    data object ReviewSavedSuccessfully : TeacherRatingViewModelEvent
    data object ReviewDeletedSuccessfully : TeacherRatingViewModelEvent
    data class ReviewSaveError(val message: UiText) : TeacherRatingViewModelEvent
}