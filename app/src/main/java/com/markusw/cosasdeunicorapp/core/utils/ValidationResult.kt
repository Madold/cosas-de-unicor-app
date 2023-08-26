package com.markusw.cosasdeunicorapp.core.utils

import com.markusw.cosasdeunicorapp.core.presentation.UiText

data class ValidationResult(
    val successful: Boolean = false,
    val errorMessage: UiText? = null
)
