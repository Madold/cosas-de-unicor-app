package com.markusw.cosasdeunicorapp.teacher_rating.domain.model

import com.markusw.cosasdeunicorapp.core.domain.model.User

data class Review(
    val content: String = "",
    val vote: TeacherRating = TeacherRating.Supportive,
    val author: User = User(),
    val likes: List<Vote> = emptyList(),
    val dislikes: List<Vote> = emptyList(),
    val timestamp: Long = 0L
)
