package com.markusw.cosasdeunicorapp.domain.use_cases

import javax.inject.Inject

class ValidateName @Inject constructor() {

    operator fun invoke(name: String): ValidationResult {

        if (name.isBlank()) {
            return ValidationResult(successful = false, errorMessage = "El nombre no puede estar vacío")
        }

        if (name.length < 3) {
            return ValidationResult(successful = false, errorMessage = "El nombre es muy corto")
        }

        return ValidationResult(successful = true)
    }

}