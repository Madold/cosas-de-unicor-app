package com.markusw.cosasdeunicorapp.core.domain.use_cases

import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import com.markusw.cosasdeunicorapp.R

class ValidatePasswordTest {

    private lateinit var validatePassword: ValidatePassword
    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun setUp() {
        validatePassword = ValidatePassword()
    }

    @Test
    fun whenPasswordIsBlankThenReturnAnError() {
        val blankPassword = ""
        val result = validatePassword(blankPassword)
        val expectedErrorMessage = context.getString(R.string.password_blank)
        val errorMessageAsString = result.errorMessage?.asString(context)

        assertFalse(result.successful)
        assertEquals(expectedErrorMessage, errorMessageAsString)
    }

    @Test
    fun whenPasswordDoesNotMeetTheMinimumLengthThenReturnAnError() {
        val shortPassword = "abc"
        val result = validatePassword(shortPassword)
        val expectedErrorMessage = context.getString(R.string.password_too_short)
        val errorMessageAsString = result.errorMessage?.asString(context)

        assertFalse(result.successful)
        assertEquals(expectedErrorMessage, errorMessageAsString)
    }

    @Test
    fun whenPasswordDoesNotContainAnUpperCaseLetterThenReturnAnError() {
        val passwordWithoutUpperCaseLetter = "abc123!@#"
        val result = validatePassword(passwordWithoutUpperCaseLetter)
        val expectedErrorMessage = context.getString(R.string.invalid_password)
        val errorMessageAsString = result.errorMessage?.asString(context)

        assertFalse(result.successful)
        assertEquals(expectedErrorMessage, errorMessageAsString)
    }
    
    @Test
    fun whenPasswordDoesNotContainANumberThenReturnAnError() {
        val passwordWithoutNumber = "Abcdef!@#"
        val result = validatePassword(passwordWithoutNumber)
        val expectedErrorMessage = context.getString(R.string.invalid_password)
        val errorMessageAsString = result.errorMessage?.asString(context)

        assertFalse(result.successful)
        assertEquals(expectedErrorMessage, errorMessageAsString)
    }

    @Test
    fun whenPasswordDoesNotContainASpecialCharacterThenReturnAnError() {
        val passwordWithoutSpecialCharacter = "Abcdef123"
        val result = validatePassword(passwordWithoutSpecialCharacter)
        val expectedErrorMessage = context.getString(R.string.invalid_password)
        val errorMessageAsString = result.errorMessage?.asString(context)

        assertFalse(result.successful)
        assertEquals(expectedErrorMessage, errorMessageAsString)
    }

    @Test
    fun whenPasswordIsValidThenReturnSuccess() {
        val validPassword = "Peter13_"
        val result = validatePassword(validPassword)

        assertTrue(result.successful)
        assertNull(result.errorMessage)
    }

}