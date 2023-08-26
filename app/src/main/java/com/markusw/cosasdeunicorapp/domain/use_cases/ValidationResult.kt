package com.markusw.cosasdeunicorapp.domain.use_cases

import com.markusw.cosasdeunicorapp.core.utils.UiText

data class ValidationResult(
    val successful: Boolean = false,
    val errorMessage: UiText? = null
)
