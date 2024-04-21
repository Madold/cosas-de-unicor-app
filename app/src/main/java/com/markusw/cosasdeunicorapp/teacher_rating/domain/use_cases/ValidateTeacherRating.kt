package com.markusw.cosasdeunicorapp.teacher_rating.domain.use_cases

import com.markusw.cosasdeunicorapp.core.presentation.UiText
import com.markusw.cosasdeunicorapp.core.utils.ValidationResult
import com.markusw.cosasdeunicorapp.teacher_rating.domain.model.TeacherRating
import javax.inject.Inject

class ValidateTeacherRating @Inject constructor() {

    operator fun invoke(teacherRating: TeacherRating?): ValidationResult {
        if (teacherRating == null) {
            return ValidationResult(
                successful = false,
                errorMessage = UiText.DynamicString("Rating cannot be empty")
            )
        }

        return ValidationResult(successful = true)
    }

}