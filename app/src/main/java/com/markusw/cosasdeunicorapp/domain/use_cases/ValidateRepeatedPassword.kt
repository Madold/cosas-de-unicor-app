package com.markusw.cosasdeunicorapp.domain.use_cases

import javax.inject.Inject

class ValidateRepeatedPassword @Inject constructor() {

    operator fun invoke(password: String, repeatedPassword: String): ValidationResult {

        if (password != repeatedPassword) {
            return ValidationResult(
                successful = false,
                errorMessage = "Las contraseñas no coinciden"
            )
        }

        return ValidationResult(successful = true)
    }

}