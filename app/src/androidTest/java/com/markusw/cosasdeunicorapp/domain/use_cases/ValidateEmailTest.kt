package com.markusw.cosasdeunicorapp.domain.use_cases

import org.junit.Before
import org.junit.Test


class ValidateEmailTest {

    private lateinit var validateEmail: ValidateEmail

    @Before
    fun setup() {
        validateEmail = ValidateEmail()
    }

    @Test
    fun When_Email_is_blank_then_returns_an_email_blank_error() {
        val expectedError = ValidationResult(
            successful = false,
            errorMessage = "El correo no puede estar en blanco"
        )
        val blankEmail = "       "
        val result = validateEmail(blankEmail)
        assert(result == expectedError)
    }

}