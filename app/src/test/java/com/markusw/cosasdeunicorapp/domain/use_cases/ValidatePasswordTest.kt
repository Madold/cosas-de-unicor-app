package com.markusw.cosasdeunicorapp.domain.use_cases

import com.markusw.cosasdeunicorapp.core.domain.use_cases.ValidatePassword
import org.junit.Before
import org.junit.Test

class ValidatePasswordTest {

    private lateinit var validatePassword: ValidatePassword

    @Before
    fun setUp() {
        validatePassword = ValidatePassword()
    }

    @Test
    fun `when password is empty then return error`() {
        val result = validatePassword("")

        assert(result.errorMessage != null)
        assert(!result.successful)
    }

    @Test
    fun `when password is less than 6 characters then return error`() {
        val result = validatePassword("12345")

        assert(result.errorMessage != null)
        assert(!result.successful)
    }

    @Test
    fun `when password does not contain a number then return error`() {
        val result = validatePassword("Password")

        assert(result.errorMessage != null)
        assert(!result.successful)
    }

    @Test
    fun `when password does not contain a special character then return error`() {
        val result = validatePassword("Password1")

        assert(result.errorMessage != null)
        assert(!result.successful)
    }

    @Test
    fun `when password does not contain a capital letter at start then return error`() {
        val result = validatePassword("password1!")

        assert(result.errorMessage != null)
        assert(!result.successful)
    }

    @Test
    fun `when password is valid then return success`() {
        val result = validatePassword("Password1!")

        assert(result.errorMessage == null)
        assert(result.successful)
    }

}