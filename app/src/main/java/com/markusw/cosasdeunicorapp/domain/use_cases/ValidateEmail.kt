package com.markusw.cosasdeunicorapp.domain.use_cases

import android.util.Patterns
import javax.inject.Inject

class ValidateEmail @Inject constructor() {

    operator fun invoke(email: String): ValidationResult {

        if (email.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "El correo no puede estar en blanco"
            )
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return ValidationResult(
                successful = false,
                errorMessage = "El correo no es un correo válido"
            )
        }

        return ValidationResult(successful = true)
    }

}