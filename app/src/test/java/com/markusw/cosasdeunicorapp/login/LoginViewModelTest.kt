
package com.markusw.cosasdeunicorapp.login

import com.google.firebase.auth.AuthCredential
import com.markusw.cosasdeunicorapp.TestDispatchers
import com.markusw.cosasdeunicorapp.auth.presentation.login.AuthenticationEvent
import com.markusw.cosasdeunicorapp.auth.presentation.login.LoginState
import com.markusw.cosasdeunicorapp.auth.presentation.login.LoginViewModel
import com.markusw.cosasdeunicorapp.core.utils.Resource
import com.markusw.cosasdeunicorapp.core.domain.AuthService
import com.markusw.cosasdeunicorapp.core.domain.use_cases.ValidateEmail
import com.markusw.cosasdeunicorapp.core.domain.use_cases.ValidatePassword
import com.markusw.cosasdeunicorapp.core.utils.ValidationResult
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test


@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    @RelaxedMockK
    private lateinit var authService: AuthService
    private lateinit var viewModel: LoginViewModel
    @RelaxedMockK
    private lateinit var validateEmail: ValidateEmail
    private lateinit var validatePassword: ValidatePassword
    private lateinit var testDispatchers: TestDispatchers
    private val fakeGoogleCredential = mockk<AuthCredential>()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        validatePassword = ValidatePassword()
        testDispatchers = TestDispatchers()
        viewModel = LoginViewModel(
            authService,
            validateEmail,
            validatePassword,
            testDispatchers
        )
    }

    @Test
    fun `Must show the initial state correctly`() {
        val expectedState = LoginState()

        assertEquals(viewModel.uiState.value, expectedState)
    }

    @Test
    fun `Must change the email and show it to the exposed state`() {
        val expectedEmail = "someEmail@gmail.com"
        viewModel.onEmailChanged(expectedEmail)
        assertEquals(viewModel.uiState.value.email, expectedEmail)
    }

    @Test
    fun `Must change the password and show it to the exposed state`() {
        val expectedPassword = "examplePassword"
        viewModel.onPasswordChanged(expectedPassword)
        assertEquals(viewModel.uiState.value.password, expectedPassword)
    }

    @Test
    fun `when user login with an invalid email then show an error in the exposed state`() {
        val invalidEmail = "myEmail"
        val expectedError = "Email is invalid"
        coEvery { validateEmail(invalidEmail) } returns ValidationResult(successful = false, expectedError)
        viewModel.onEmailChanged(invalidEmail)
        viewModel.onLogin()
        assertEquals(viewModel.uiState.value.emailError,  expectedError)
    }

    @Test
    fun `When user login with a blank password then shows a password blank error in the exposed state`() {
        val invalidPassword = ""
        val expectedError = "La contraseña no puede ser vacía"
        viewModel.onPasswordChanged(invalidPassword)
        viewModel.onLogin()
        assertEquals(viewModel.uiState.value.passwordError, expectedError)
    }

    @Test
    fun `when user login with an invalid password then shows a password invalid error in the exposed state`() {
        val invalidPassword = "invalidPassword"
        val expectedError = "La contraseña debe empezar por mayuscula, tener almenos un número y un caracter especial"
        viewModel.onPasswordChanged(invalidPassword)
        viewModel.onLogin()
        assertEquals(viewModel.uiState.value.passwordError, expectedError)
    }

    @Test
    fun `when user login with a password length minus than 6 then shows an error in the exposed state`() {
        val shortPassword = "abc"
        val expectedError = "La contraseña debe tener al menos 6 caracteres de longitud"
        viewModel.onPasswordChanged(shortPassword)
        viewModel.onLogin()
        assertEquals(viewModel.uiState.value.passwordError, expectedError)
    }

    @Test
    fun `when user login with invalid credentials and then login with valid credentials email and password errors should be null`() {
        val invalidEmail = "invalidEmail"
        val invalidPassword = "invalidPassword"
        coEvery { validateEmail(invalidEmail) } returns ValidationResult(successful = false, errorMessage = "invalid email")
        val validEmail = "validEmail@gmai.com"
        val validPassword = "Password1!"
        viewModel.onEmailChanged(invalidEmail)
        viewModel.onPasswordChanged(invalidPassword)
        viewModel.onLogin()

        assert(viewModel.uiState.value.emailError == "invalid email" && viewModel.uiState.value.passwordError == "La contraseña debe empezar por mayuscula, tener almenos un número y un caracter especial")
        coEvery { validateEmail(validEmail) } returns ValidationResult(successful = true)

        viewModel.onEmailChanged(validEmail)
        viewModel.onPasswordChanged(validPassword)
        viewModel.onLogin()
        assert(viewModel.uiState.value.emailError == null && viewModel.uiState.value.passwordError == null)
    }

    @Test
    fun `When auth service return Resource Error then the ViewModel emits an authentication error event`() = runTest {
        val expectedEmail = "exampleEmail@gmail.com"
        val expectedPassword = "Password1!"
        val expectedError = Resource.Error<Unit>("Example error")
        coEvery { authService.authenticate(expectedEmail, expectedPassword) } returns expectedError
        coEvery { validateEmail(expectedEmail) } returns ValidationResult(successful = true)

        viewModel.onEmailChanged(expectedEmail)
        viewModel.onPasswordChanged(expectedPassword)
        viewModel.onLogin()

        assertEquals(viewModel.authenticationEvents.first(), AuthenticationEvent.AuthFailed(reason = expectedError.message!!))
    }

    @Test
    fun `When auth service return Resource Success then the ViewModel emits an authentication success event`() = runTest {
        val expectedEmail = "exampleEmail@gmail.com"
        val expectedPassword = "Password1!"
        val expectedEvent = Resource.Success(Unit)
        coEvery { authService.authenticate(expectedEmail, expectedPassword) } returns expectedEvent
        coEvery { validateEmail(expectedEmail) } returns ValidationResult(successful = true)

        viewModel.onEmailChanged(expectedEmail)
        viewModel.onPasswordChanged(expectedPassword)
        viewModel.onLogin()

        assertEquals(viewModel.authenticationEvents.first(), AuthenticationEvent.AuthSuccessful)
    }

    @Test
    fun `When user sign in with google and google auth service returns error then ViewModel emits an authentication error event`() = runTest {
        val expectedError = Resource.Error<Unit>("Invalid credentials")
        coEvery { authService.authenticateWithCredential(fakeGoogleCredential) } returns expectedError
        viewModel.onGoogleSignInResult(fakeGoogleCredential)

        assertEquals(viewModel.authenticationEvents.first(), AuthenticationEvent.AuthFailed(reason = "Invalid credentials"))
    }

    @Test
    fun `When user sign in with google and google auth service returns success then ViewModel emits an authentication success event`() = runTest {
        val expectedResult = Resource.Success(Unit)
        coEvery { authService.authenticateWithCredential(fakeGoogleCredential) } returns expectedResult
        viewModel.onGoogleSignInResult(fakeGoogleCredential)

        assertEquals(viewModel.authenticationEvents.first(), AuthenticationEvent.AuthSuccessful)
    }

}