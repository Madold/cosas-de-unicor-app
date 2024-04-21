package com.markusw.cosasdeunicorapp.teacher_rating.data.model

import com.markusw.cosasdeunicorapp.teacher_rating.domain.model.TeacherRating
import com.markusw.cosasdeunicorapp.teacher_rating.domain.model.Vote

data class ReviewDto(
    val content: String = "",
    val vote: TeacherRating = TeacherRating.Supportive,
    val authorId: String = "",
    val likes: List<Vote> = emptyList(),
    val dislikes: List<Vote> = emptyList(),
    val timestamp: Long = 0L
)
