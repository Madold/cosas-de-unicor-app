package com.markusw.cosasdeunicorapp.teacher_rating.data.repository

import com.markusw.cosasdeunicorapp.core.utils.Result
import com.markusw.cosasdeunicorapp.teacher_rating.domain.model.Review
import com.markusw.cosasdeunicorapp.teacher_rating.domain.model.TeacherReview
import com.markusw.cosasdeunicorapp.teacher_rating.domain.repository.TeacherRatingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeRatingRepository : TeacherRatingRepository {
    override fun getTeachers(): Flow<List<TeacherReview>> {
        return flow {}
    }

    override suspend fun saveReview(review: Review, teacherId: String): Result<Unit> {
        return Result.Success(Unit)
    }

    override suspend fun deleteReview(review: Review, teacherId: String): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun toggleReviewLike(teacherId: String, authorId: String): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun toggleReviewDislike(teacherId: String, authorId: String): Result<Unit> {
        TODO("Not yet implemented")
    }
}