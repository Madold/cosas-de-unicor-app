package com.markusw.cosasdeunicorapp.teacher_rating.data.model

import com.markusw.cosasdeunicorapp.teacher_rating.domain.model.TeacherRating

data class ReviewDto(
    val content: String = "",
    val vote: TeacherRating = TeacherRating.Supportive,
    val authorId: String = "",
    val likes: List<String> = emptyList(),
    val dislikes: List<String> = emptyList(),
    val timestamp: Long = 0L
)
