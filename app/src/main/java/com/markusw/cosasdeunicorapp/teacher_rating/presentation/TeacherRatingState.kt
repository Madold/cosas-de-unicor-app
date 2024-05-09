package com.markusw.cosasdeunicorapp.teacher_rating.presentation

import com.markusw.cosasdeunicorapp.core.domain.model.User
import com.markusw.cosasdeunicorapp.teacher_rating.domain.model.TeacherRating
import com.markusw.cosasdeunicorapp.teacher_rating.domain.model.TeacherReview
import com.markusw.cosasdeunicorapp.teacher_rating.presentation.composables.AZ

data class TeacherRatingState(
    val teachers: List<TeacherReview> = emptyList(),
    val userOpinion: String = "",
    val selectedTeacherRating: TeacherRating = TeacherRating.Supportive,
    val isSavingReview: Boolean = false,
    val selectedTeacher: TeacherReview = TeacherReview(),
    val isLoadingReviews: Boolean = false,
    val loggedUser: User = User(),
    val isDeletingReview: Boolean = false,
    val isSearchBarActive: Boolean = false,
    val teacherNameQuery: String = "",
    val filteredTeachers: List<TeacherReview> = emptyList(),
    val isTeacherFiltersDialogVisible: Boolean = false,
    val filterType: FilterType = FilterType.ByNameAscending,
    val isNameOrderDropDownMenuExpanded: Boolean = false,
    val selectedNameOrderOption: String = AZ,
)
