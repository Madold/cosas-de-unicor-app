package com.markusw.cosasdeunicorapp.domain.use_cases

data class ValidationResult(
    val successful: Boolean = false,
    val errorMessage: String? = null
)
