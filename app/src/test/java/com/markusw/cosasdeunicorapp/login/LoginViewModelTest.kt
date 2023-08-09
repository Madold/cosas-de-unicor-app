@file:OptIn(ExperimentalCoroutinesApi::class)

package com.markusw.cosasdeunicorapp.login

import com.markusw.cosasdeunicorapp.core.utils.Resource
import com.markusw.cosasdeunicorapp.domain.services.AuthService
import com.markusw.cosasdeunicorapp.domain.use_cases.ValidateEmail
import com.markusw.cosasdeunicorapp.domain.use_cases.ValidatePassword
import com.markusw.cosasdeunicorapp.domain.use_cases.ValidationResult
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class LoginViewModelTest {

    @RelaxedMockK
    private lateinit var authService: AuthService
    private lateinit var viewModel: LoginViewModel
    @RelaxedMockK
    private lateinit var validateEmail: ValidateEmail
    private lateinit var validatePassword: ValidatePassword

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        validatePassword = ValidatePassword()
        viewModel = LoginViewModel(authService, validateEmail, validatePassword)
    }

    @Test
    fun `Must show the initial state correctly`() {
        val expectedState = LoginState()

        assert(viewModel.uiState.value == expectedState)
    }

    @Test
    fun `Must change the password and show it to the exposed state`() {
        val expectedPassword = "examplePassword"
        viewModel.onPasswordChanged(expectedPassword)
        assert(viewModel.uiState.value.password == expectedPassword)
    }

    @Test
    fun `When auth service return Resource Error then the ViewModel emits an authentication error`() = runTest {
        val expectedEmail = "exampleEmail@gmail.com"
        val expectedPassword = "Password1!"
        val expectedError = Resource.Error<Unit>("Example error")
        coEvery { authService.authenticate(expectedEmail, expectedPassword) } returns expectedError
        coEvery { validateEmail(expectedEmail) } returns ValidationResult(successful = true)

        viewModel.onEmailChanged(expectedEmail)
        viewModel.onPasswordChanged(expectedPassword)
        viewModel.onLogin()

        assert(viewModel.authenticationEvents.first() == AuthenticationEvent.AuthFailed(reason = expectedError.message!!))
    }

}