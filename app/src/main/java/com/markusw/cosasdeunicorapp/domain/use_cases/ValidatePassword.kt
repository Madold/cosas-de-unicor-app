package com.markusw.cosasdeunicorapp.domain.use_cases

import javax.inject.Inject

class ValidatePassword @Inject constructor() {

    companion object {
        const val PASSWORD_REGEX =
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{6,}\$"
        const val MINIMUM_PASSWORD_LENGTH = 6
    }

    operator fun invoke(password: String): ValidationResult {

        if (password.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "La contraseña no puede ser vacía"
            )
        }

        if (password.length < MINIMUM_PASSWORD_LENGTH) {
            return ValidationResult(
                successful = false,
                errorMessage = "La contraseña debe tener al menos 6 caracteres de longitud"
            )
        }

        if (!password.matches(Regex(PASSWORD_REGEX))) {
            return ValidationResult(
                successful = false,
                errorMessage = "La contraseña debe empezar por mayuscula, tener almenos un número y un caracter especial"
            )
        }

        return ValidationResult(successful = true)
    }

}