package com.markusw.cosasdeunicorapp.teacher_rating.data.model

data class TeacherReviewDto(
    val id: String = "",
    val teacherName: String = "",
    val reviews: List<ReviewDto> = emptyList()
)