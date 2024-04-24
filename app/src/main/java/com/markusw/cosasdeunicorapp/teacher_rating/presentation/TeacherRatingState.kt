package com.markusw.cosasdeunicorapp.teacher_rating.presentation

import com.markusw.cosasdeunicorapp.core.domain.model.User
import com.markusw.cosasdeunicorapp.teacher_rating.domain.model.TeacherRating
import com.markusw.cosasdeunicorapp.teacher_rating.domain.model.TeacherReview

data class TeacherRatingState(
    val teachers: List<TeacherReview> = emptyList(),
    val userOpinion: String = "",
    val selectedTeacherRating: TeacherRating = TeacherRating.Supportive,
    val isSavingReview: Boolean = false,
    val selectedTeacher: TeacherReview = TeacherReview(),
    val isLoadingReviews: Boolean = false,
    val loggedUser: User = User(),
    val isDeletingReview: Boolean = false
)
