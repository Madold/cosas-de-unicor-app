package com.markusw.cosasdeunicorapp.teacher_rating.presentation

import com.markusw.cosasdeunicorapp.teacher_rating.domain.model.Review
import com.markusw.cosasdeunicorapp.teacher_rating.domain.model.TeacherRating
import com.markusw.cosasdeunicorapp.teacher_rating.domain.model.TeacherReview

sealed interface TeacherRatingEvent {
    data class ChangeUserOpinion(val opinion: String) : TeacherRatingEvent
    data class ChangeTeacherRating(val teacherRating: TeacherRating) : TeacherRatingEvent

    data class ChangeSelectedTeacher(val teacher: TeacherReview) : TeacherRatingEvent
    data object SubmitRating : TeacherRatingEvent
    data class DeleteReview(val review: Review) : TeacherRatingEvent
}