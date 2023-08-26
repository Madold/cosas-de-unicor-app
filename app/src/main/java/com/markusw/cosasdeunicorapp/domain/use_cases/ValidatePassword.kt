package com.markusw.cosasdeunicorapp.domain.use_cases

import com.markusw.cosasdeunicorapp.R
import com.markusw.cosasdeunicorapp.core.utils.UiText
import javax.inject.Inject

class ValidatePassword @Inject constructor() {

    companion object {
        const val PASSWORD_REGEX =
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*._?&])[A-Za-z\\d@\$!%*._?&]{6,}\$"
        const val MINIMUM_PASSWORD_LENGTH = 6
    }

    operator fun invoke(password: String): ValidationResult {

        if (password.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = UiText.StringResource(R.string.password_blank)
            )
        }

        if (password.length < MINIMUM_PASSWORD_LENGTH) {
            return ValidationResult(
                successful = false,
                errorMessage = UiText.StringResource(R.string.password_too_short)
            )
        }

        if (!password.matches(Regex(PASSWORD_REGEX))) {
            return ValidationResult(
                successful = false,
                errorMessage = UiText.StringResource(R.string.invalid_password)
            )
        }

        return ValidationResult(successful = true)
    }

}