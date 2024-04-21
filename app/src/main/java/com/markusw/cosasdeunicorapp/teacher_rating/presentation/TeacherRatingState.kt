package com.markusw.cosasdeunicorapp.teacher_rating.presentation

import com.markusw.cosasdeunicorapp.core.presentation.UiText
import com.markusw.cosasdeunicorapp.teacher_rating.domain.model.TeacherRating
import com.markusw.cosasdeunicorapp.teacher_rating.domain.model.TeacherReview

data class TeacherRatingState(
    val teachers: List<TeacherReview> = emptyList(),
    val userOpinion: String = "",
    val selectedTeacherRating: TeacherRating = TeacherRating.Supportive,
    val isSavingReview: Boolean = false,
    val opinionError: UiText? = null,
    val ratingError: UiText? = null,
    val selectedTeacher: TeacherReview = TeacherReview(),
    val isLoadingReviews: Boolean = false
)
