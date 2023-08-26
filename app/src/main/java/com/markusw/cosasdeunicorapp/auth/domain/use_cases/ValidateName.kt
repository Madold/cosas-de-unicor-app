package com.markusw.cosasdeunicorapp.auth.domain.use_cases

import com.markusw.cosasdeunicorapp.R
import com.markusw.cosasdeunicorapp.core.presentation.UiText
import com.markusw.cosasdeunicorapp.core.utils.ValidationResult
import javax.inject.Inject

class ValidateName @Inject constructor() {

    companion object {
        const val MIN_NAME_LENGTH = 3
    }

    operator fun invoke(name: String): ValidationResult {

        if (name.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = UiText.StringResource(R.string.name_blank)
            )
        }

        if (name.length < MIN_NAME_LENGTH) {
            return ValidationResult(
                successful = false,
                errorMessage = UiText.StringResource(R.string.name_too_short)
            )
        }

        return ValidationResult(successful = true)
    }

}