package com.markusw.cosasdeunicorapp.core.domain.use_cases

import com.markusw.cosasdeunicorapp.R
import com.markusw.cosasdeunicorapp.core.presentation.UiText
import com.markusw.cosasdeunicorapp.core.utils.ValidationResult
import javax.inject.Inject

class ValidateEmail @Inject constructor() {

    companion object {
        private const val EMAIL_REGEX = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"
    }

    operator fun invoke(email: String): ValidationResult {

        if (email.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = UiText.StringResource(R.string.blank_email_error)
            )
        }

        if (!email.matches(EMAIL_REGEX.toRegex())) {
            return ValidationResult(
                successful = false,
                errorMessage = UiText.StringResource(R.string.invalid_email_error)
            )
        }

        return ValidationResult(successful = true)
    }

}