package com.markusw.cosasdeunicorapp.auth.presentation.resetpassword

import com.markusw.cosasdeunicorapp.core.presentation.UiText

data class ResetPasswordState(
    val email: String = "",
    val emailError: UiText? = null,
    val isLoading: Boolean = false
)
