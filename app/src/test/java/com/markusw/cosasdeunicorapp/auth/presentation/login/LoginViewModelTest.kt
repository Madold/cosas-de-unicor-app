@file:OptIn(ExperimentalCoroutinesApi::class)

package com.markusw.cosasdeunicorapp.auth.presentation.login

import app.cash.turbine.test
import com.markusw.cosasdeunicorapp.auth.domain.use_cases.LoginWithCredential
import com.markusw.cosasdeunicorapp.auth.domain.use_cases.LoginWithEmailAndPassword
import com.markusw.cosasdeunicorapp.core.DispatcherProvider
import com.markusw.cosasdeunicorapp.core.TestDispatchers
import com.markusw.cosasdeunicorapp.core.data.repository.FakeAuthRepository
import com.markusw.cosasdeunicorapp.core.domain.repository.AuthRepository
import com.markusw.cosasdeunicorapp.core.domain.use_cases.ValidateEmail
import com.markusw.cosasdeunicorapp.core.domain.use_cases.ValidatePassword
import com.markusw.cosasdeunicorapp.core.presentation.UiText
import com.markusw.cosasdeunicorapp.core.utils.ValidationResult
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import kotlin.math.exp

class LoginViewModelTest {

    private lateinit var fakeAuthRepository: AuthRepository
    private lateinit var loginWithEmailAndPasswordFake: LoginWithEmailAndPassword
    private lateinit var loginWithCredentialFake: LoginWithCredential
    @RelaxedMockK
    private lateinit var validateEmailFake: ValidateEmail
    @RelaxedMockK
    private lateinit var validatePasswordFake: ValidatePassword
    private lateinit var viewModel: LoginViewModel
    private val testDispatcher: DispatcherProvider = TestDispatchers()
    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(dispatcher)
        fakeAuthRepository = FakeAuthRepository()
        loginWithEmailAndPasswordFake = LoginWithEmailAndPassword(fakeAuthRepository)
        loginWithCredentialFake = LoginWithCredential(fakeAuthRepository)
        viewModel = LoginViewModel(
            loginWithEmailAndPasswordFake,
            loginWithCredentialFake,
            validateEmailFake,
            validatePasswordFake,
            testDispatcher
        )
    }

    @Test
    fun shouldHaveTheCorrectInitialState() {
        val initialState = viewModel.uiState.value
        val expectedState = LoginState()
        assertEquals(expectedState, initialState)
    }

    @Test
    fun shouldUpdateEmail() {
        val email = "randomEmail@gmail.com"
        viewModel.onEmailChanged(email)
        val currentState = viewModel.uiState.value
        val expectedState = LoginState(email = email)
        assertEquals(expectedState, currentState)
    }

    @Test
    fun shouldUpdatePassword() {
        val password = "randomPassword"
        viewModel.onPasswordChanged(password)
        val currentState = viewModel.uiState.value
        val expectedState = LoginState(password = password)
        assertEquals(expectedState, currentState)
    }

    @Test
    fun on_google_signIn_started_should_put_loading_state_to_true() {
        viewModel.onGoogleSignInStarted()
        val currentState = viewModel.uiState.value
        val expectedState = LoginState(isLoading = true)
        assertEquals(expectedState, currentState)
    }

    @Test
    fun on_google_signIn_finished_should_put_loading_state_to_false() {
        viewModel.onGoogleSignInFinished()
        val currentState = viewModel.uiState.value
        val expectedState = LoginState(isLoading = false)
        assertEquals(expectedState, currentState)
    }

    @Test
    fun `when email validation is not success then is loading should be false`() {
        every { validateEmailFake(any()) } returns ValidationResult(successful = false, errorMessage = UiText.DynamicString("Invalid email"))
        viewModel.onLogin()
        val currentState = viewModel.uiState.value
        val expectedValue = false

        assertEquals(currentState.isLoading, expectedValue)
    }

    @Test
    fun `when password validation is not success then is loading should be false`() {
        every { validatePasswordFake(any()) } returns ValidationResult(successful = false, errorMessage = UiText.DynamicString("Invalid password"))
        viewModel.onLogin()
        val currentState = viewModel.uiState.value
        val expectedValue = false

        assertEquals(currentState.isLoading, expectedValue)
    }

    @Test
    fun `when password validation and email validation is success then error messages should be null`() {
        every { validateEmailFake(any()) } returns ValidationResult(successful = true)
        every { validatePasswordFake(any()) } returns ValidationResult(successful = true)

        viewModel.onLogin()
        val currentState = viewModel.uiState.value
        val expectedState = LoginState(
            passwordError = null,
            emailError = null
        )

        assertEquals(currentState, expectedState)
    }


}
