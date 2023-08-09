package com.markusw.cosasdeunicorapp.register

data class RegisterState(
    val name: String = "",
    val nameError: String? = null,
    val email: String = "",
    val emailError: String? = null,
    val password: String = "",
    val passwordError: String? = null,
    val repeatedPassword: String = "",
    val repeatedPasswordError: String? = null,
    val isTermsAccepted: Boolean = false,
    val termsError: String? = null,
    val isLoading: Boolean = false
)
