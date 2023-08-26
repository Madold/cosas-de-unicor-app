package com.markusw.cosasdeunicorapp.auth.presentation.register

import com.markusw.cosasdeunicorapp.core.presentation.UiText

data class RegisterState(
    val name: String = "",
    val nameError: UiText? = null,
    val email: String = "",
    val emailError: UiText? = null,
    val password: String = "",
    val passwordError: UiText? = null,
    val repeatedPassword: String = "",
    val repeatedPasswordError: UiText? = null,
    val isTermsAccepted: Boolean = false,
    val termsError: UiText? = null,
    val isLoading: Boolean = false
)
