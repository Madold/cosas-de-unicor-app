package com.markusw.cosasdeunicorapp.login

import com.markusw.cosasdeunicorapp.core.utils.UiText

data class LoginState(
    val email: String = "",
    val emailError: UiText? = null,
    val password: String = "",
    val passwordError: UiText? = null,
    val isLoading: Boolean = false
)
