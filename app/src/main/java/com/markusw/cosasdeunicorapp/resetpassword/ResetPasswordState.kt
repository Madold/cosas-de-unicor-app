package com.markusw.cosasdeunicorapp.resetpassword

import com.markusw.cosasdeunicorapp.core.utils.UiText

data class ResetPasswordState(
    val email: String = "",
    val emailError: UiText? = null,
    val isLoading: Boolean = false
)
