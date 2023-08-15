package com.markusw.cosasdeunicorapp.resetpassword

data class ResetPasswordState(
    val email: String = "",
    val emailError: String? = null,
    val isLoading: Boolean = false
)
