package com.markusw.cosasdeunicorapp.core.domain.use_cases

import android.util.Patterns
import com.markusw.cosasdeunicorapp.core.presentation.UiText
import javax.inject.Inject
import com.markusw.cosasdeunicorapp.R
import com.markusw.cosasdeunicorapp.core.utils.ValidationResult

class ValidateEmail @Inject constructor() {

    operator fun invoke(email: String): ValidationResult {

        if (email.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = UiText.StringResource(R.string.blank_email_error)
            )
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return ValidationResult(
                successful = false,
                errorMessage = UiText.StringResource(R.string.invalid_email_error)
            )
        }

        return ValidationResult(successful = true)
    }

}