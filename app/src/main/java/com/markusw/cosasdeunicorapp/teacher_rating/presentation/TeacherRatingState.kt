package com.markusw.cosasdeunicorapp.teacher_rating.presentation

import com.markusw.cosasdeunicorapp.teacher_rating.domain.model.TeacherRating
import com.markusw.cosasdeunicorapp.teacher_rating.domain.model.TeacherReview

data class TeacherRatingState(
    val teachers: List<TeacherReview> = emptyList(),
    val userOpinion: String = "",
    val selectedTeacherRating: TeacherRating? = null
)
