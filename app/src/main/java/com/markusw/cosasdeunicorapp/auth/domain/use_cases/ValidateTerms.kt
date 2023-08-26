package com.markusw.cosasdeunicorapp.auth.domain.use_cases

import com.markusw.cosasdeunicorapp.R
import com.markusw.cosasdeunicorapp.core.presentation.UiText
import com.markusw.cosasdeunicorapp.core.utils.ValidationResult
import javax.inject.Inject

class ValidateTerms @Inject constructor() {

    operator fun invoke(isAccepted: Boolean): ValidationResult {

        if (!isAccepted) {
            return ValidationResult(
                successful = false,
                errorMessage = UiText.StringResource(R.string.terms_not_accepted)
            )
        }

        return ValidationResult(successful = true)
    }

}