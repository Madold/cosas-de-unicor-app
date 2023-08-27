package com.markusw.cosasdeunicorapp.core.domain.use_cases

import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import com.markusw.cosasdeunicorapp.R

class ValidateEmailTest {

    private lateinit var validateEmail: ValidateEmail
    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun setUp() {
        validateEmail = ValidateEmail()
    }

    @Test
    fun whenEmailIsBlankThenReturnError() {
        val email = ""
        val expectedErrorMessage = context.getString(R.string.blank_email_error)
        val result = validateEmail(email)
        val errorMessageAsString = result.errorMessage?.asString(context)

        assert(result.successful.not())
        assertEquals(errorMessageAsString, expectedErrorMessage)
    }

    @Test
    fun whenEmailIsNotAValidEmailThenReturnError() {
        val email = "notavalidemail"
        val expectedErrorMessage = context.getString(R.string.invalid_email_error)
        val result = validateEmail(email)
        val errorMessageAsString = result.errorMessage?.asString(context)

        assert(result.successful.not())
        assertEquals(errorMessageAsString, expectedErrorMessage)
    }

    @Test
    fun whenEmailIsValidThenReturnSuccess() {
        val email = "myemail@google.com"
        val result = validateEmail(email)

        assert(result.successful)
        assertNull(result.errorMessage)
    }

}