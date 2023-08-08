package com.markusw.cosasdeunicorapp.domain.use_cases

import javax.inject.Inject

class ValidatePassword @Inject constructor() {

    companion object {
        const val PASSWORD_REGEX = "^(?=.*[A-Z])(?=.*\\d)(?=.*[!@#\$%^&*()\\-_=+{}[\\]:;<>,.?])(.{6,})\$"
    }

    operator fun invoke(password: String): ValidationResult {

        if (password.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "La contraseña no puede ser vacía"
            )
        }

        val regex = Regex(PASSWORD_REGEX)

        if (!regex.matches(password)) {
            return ValidationResult(
                successful = false,
                errorMessage = "La contraseña debe tener al menos 6 caracteres de longitud, empezar por mayuscula, tener almenos un número y un caracter especial"
            )
        }

        return ValidationResult(successful = true)
    }

}