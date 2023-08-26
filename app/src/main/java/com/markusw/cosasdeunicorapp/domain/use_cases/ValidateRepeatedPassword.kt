package com.markusw.cosasdeunicorapp.domain.use_cases

import com.markusw.cosasdeunicorapp.R
import com.markusw.cosasdeunicorapp.core.utils.UiText
import javax.inject.Inject

class ValidateRepeatedPassword @Inject constructor() {

    operator fun invoke(password: String, repeatedPassword: String): ValidationResult {

        if (password != repeatedPassword) {
            return ValidationResult(
                successful = false,
                errorMessage = UiText.StringResource(R.string.passwords_do_not_match)
            )
        }

        return ValidationResult(successful = true)
    }

}