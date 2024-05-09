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
    data object ShowSearchBar : TeacherRatingEvent
    data object HideSearchBar : TeacherRatingEvent
    data class ChangeTeacherNameQuery(val query: String) : TeacherRatingEvent
    data object SearchTeachers : TeacherRatingEvent
    data class ToggleReviewLike(val teacherId: String, val authorId: String): TeacherRatingEvent
    data class ToggleReviewDislike(val teacherId: String, val authorId: String): TeacherRatingEvent

    data object ShowTeacherFiltersDialog : TeacherRatingEvent
    data object HideTeacherFiltersDialog : TeacherRatingEvent
    data class ChangeFilterType(val filterType: FilterType) : TeacherRatingEvent
    data class ChangeNameOrderDropDownMenuExpanded(val isExpanded: Boolean) : TeacherRatingEvent
    data class ChangeNameOrderDropDownMenuOption(val option: String) : TeacherRatingEvent
}