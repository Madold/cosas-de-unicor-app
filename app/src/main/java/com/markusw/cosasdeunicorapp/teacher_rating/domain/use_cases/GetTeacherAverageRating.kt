package com.markusw.cosasdeunicorapp.teacher_rating.domain.use_cases

import com.markusw.cosasdeunicorapp.teacher_rating.domain.model.TeacherRating
import com.markusw.cosasdeunicorapp.teacher_rating.domain.model.TeacherReview
import javax.inject.Inject

class GetTeacherAverageRating @Inject constructor() {
    operator fun invoke(teacher: TeacherReview): TeacherRating {
        val homieReviewsCount = teacher.reviews.count { it.vote == TeacherRating.Homie }
        val pushyReviewsCount = teacher.reviews.count { it.vote == TeacherRating.Pushy }
        val supportiveReviewsCount = teacher.reviews.count { it.vote == TeacherRating.Supportive }
        val ruthlessReviewsCount = teacher.reviews.count { it.vote == TeacherRating.Ruthless }

        val maxRating = listOf(
            homieReviewsCount,
            pushyReviewsCount,
            supportiveReviewsCount,
            ruthlessReviewsCount
        ).maxOrNull() ?: 0

        return when (maxRating) {
            homieReviewsCount -> TeacherRating.Homie
            pushyReviewsCount -> TeacherRating.Pushy
            supportiveReviewsCount -> TeacherRating.Supportive
            ruthlessReviewsCount -> TeacherRating.Ruthless
            else -> TeacherRating.Supportive
        }
    }

}