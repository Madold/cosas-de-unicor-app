package com.markusw.cosasdeunicorapp.auth.presentation.login

import com.markusw.cosasdeunicorapp.core.presentation.UiText

data class LoginState(
    val email: String = "",
    val emailError: UiText? = null,
    val password: String = "",
    val passwordError: UiText? = null,
    val isLoading: Boolean = false
)
