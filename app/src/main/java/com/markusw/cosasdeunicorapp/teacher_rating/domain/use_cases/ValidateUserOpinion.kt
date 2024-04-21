package com.markusw.cosasdeunicorapp.teacher_rating.domain.use_cases

import com.markusw.cosasdeunicorapp.core.presentation.UiText
import com.markusw.cosasdeunicorapp.core.utils.ValidationResult
import javax.inject.Inject

class ValidateUserOpinion @Inject constructor() {

    operator fun invoke(userOpinion: String): ValidationResult {
        if (userOpinion.isEmpty()) {
            return ValidationResult(
                successful = false,
                errorMessage = UiText.DynamicString("Opinion cannot be empty")
            )
        }

        return ValidationResult(successful = true)
    }

}