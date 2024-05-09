package com.markusw.cosasdeunicorapp.teacher_rating.domain.model

data class TeacherReview(
    val id: String = "",
    val teacherName: String = "",
    val reviews: List<Review> = emptyList()
)
