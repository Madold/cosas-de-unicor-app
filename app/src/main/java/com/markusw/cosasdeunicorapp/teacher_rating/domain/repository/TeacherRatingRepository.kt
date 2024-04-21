package com.markusw.cosasdeunicorapp.teacher_rating.domain.repository

import com.markusw.cosasdeunicorapp.core.utils.Result
import com.markusw.cosasdeunicorapp.teacher_rating.domain.model.Review
import com.markusw.cosasdeunicorapp.teacher_rating.domain.model.TeacherReview
import kotlinx.coroutines.flow.Flow

interface TeacherRatingRepository {
    suspend fun getTeachers(): List<TeacherReview>
    suspend fun saveReview(review: Review, teacherId: String): Result<Unit>
}