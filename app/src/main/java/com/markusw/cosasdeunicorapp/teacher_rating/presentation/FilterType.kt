package com.markusw.cosasdeunicorapp.teacher_rating.presentation

import com.markusw.cosasdeunicorapp.teacher_rating.domain.model.TeacherRating

sealed interface FilterType {

    data class ByRating(val rating: TeacherRating) : FilterType
    data object ByNameAscending : FilterType
    data object ByNameDescending : FilterType
    data object Default : FilterType
}