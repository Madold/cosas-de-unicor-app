package com.markusw.cosasdeunicorapp.teacher_rating.data.repository

import com.markusw.cosasdeunicorapp.core.domain.RemoteDatabase
import com.markusw.cosasdeunicorapp.core.presentation.UiText
import com.markusw.cosasdeunicorapp.core.utils.Result
import com.markusw.cosasdeunicorapp.teacher_rating.domain.model.Review
import com.markusw.cosasdeunicorapp.teacher_rating.domain.model.TeacherReview
import com.markusw.cosasdeunicorapp.teacher_rating.domain.model.toRemoteDatabaseModel
import com.markusw.cosasdeunicorapp.teacher_rating.domain.repository.TeacherRatingRepository
import kotlinx.coroutines.flow.Flow

class AndroidRatingRepository(
    private val remoteDatabase: RemoteDatabase
): TeacherRatingRepository {
    override fun getTeachers() = remoteDatabase.getTeachers()

    override suspend fun saveReview(review: Review, teacherId: String): Result<Unit> {
        return try {
            remoteDatabase.saveReview(review.toRemoteDatabaseModel(), teacherId)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(UiText.DynamicString("Error saving review: ${e.message}"))
        }
    }

    override suspend fun deleteReview(review: Review, teacherId: String): Result<Unit> {
        return try {
            remoteDatabase.deleteReview(review, teacherId)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(UiText.DynamicString("Error deleting review: ${e.message}"))
        }
    }
}