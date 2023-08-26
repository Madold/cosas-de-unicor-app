package com.markusw.cosasdeunicorapp.domain.use_cases

import com.markusw.cosasdeunicorapp.core.domain.use_cases.ValidateEmail
import com.markusw.cosasdeunicorapp.core.utils.ValidationResult
import junit.framework.TestCase.assertEquals
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
        assertEquals(result, expectedError)
    }

    @Test
    fun when_Email_is_not_a_valid_email_then_returns_a_invalid_email_error() {
        val expectedError = ValidationResult(
            successful = false,
            errorMessage = "El correo no es un correo válido"
        )
        val invalidEmail = "invalidEmail@"
        val result = validateEmail(invalidEmail)
        assertEquals(result, expectedError)
    }

    @Test
    fun when_Email_is_a_valid_email_then_returns_a_validation_result_that_is_success() {
        val expectedResult = ValidationResult(successful = true)
        val validEmail = "validEmail@somecompany.com"
        val result = validateEmail(validEmail)
        assertEquals(result, expectedResult)
    }

}