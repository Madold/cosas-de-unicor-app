package com.markusw.cosasdeunicorapp.domain.use_cases

import javax.inject.Inject

class ValidateTerms @Inject constructor() {

    operator fun invoke(isAccepted: Boolean): ValidationResult {

        if (!isAccepted) {
            return ValidationResult(
                successful = false,
                errorMessage = "Debes aceptar los términos y condiciones para registrarte"
            )
        }

        return ValidationResult(successful = true)
    }

}