package com.markusw.cosasdeunicorapp.auth.presentation.login

import com.google.firebase.auth.AuthCredential
import com.markusw.cosasdeunicorapp.auth.domain.use_cases.LoginWithCredential
import com.markusw.cosasdeunicorapp.auth.domain.use_cases.LoginWithEmailAndPassword
import com.markusw.cosasdeunicorapp.core.DispatcherProvider
import com.markusw.cosasdeunicorapp.core.TestDispatchers
import com.markusw.cosasdeunicorapp.core.domain.ProfileUpdateData
import com.markusw.cosasdeunicorapp.core.domain.model.User
import com.markusw.cosasdeunicorapp.core.domain.repository.AuthRepository
import com.markusw.cosasdeunicorapp.core.domain.use_cases.ValidateEmail
import com.markusw.cosasdeunicorapp.core.domain.use_cases.ValidatePassword
import com.markusw.cosasdeunicorapp.core.utils.Result
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class LoginViewModelTest {

    private lateinit var fakeAuthRepository: AuthRepository
    private lateinit var loginWithEmailAndPasswordFake: LoginWithEmailAndPassword
    private lateinit var loginWithCredentialFake: LoginWithCredential
    private lateinit var validateEmailFake: ValidateEmail
    private lateinit var validatePasswordFake: ValidatePassword
    private lateinit var viewModel: LoginViewModel
    private val testDispatcher: DispatcherProvider = TestDispatchers()

    @Before
    fun setUp() {
        fakeAuthRepository = object: AuthRepository{
            override suspend fun authenticate(email: String, password: String): Result<Unit> {
                return Result.Success(Unit)
            }

            override suspend fun register(
                name: String,
                email: String,
                password: String
            ): Result<Unit> {
                return Result.Success(Unit)
            }

            override suspend fun logout(): Result<Unit> {
                return Result.Success(Unit)
            }

            override suspend fun authenticateWithCredential(credential: AuthCredential): Result<Unit> {
                return Result.Success(Unit)
            }

            override suspend fun sendPasswordResetByEmail(email: String): Result<Unit> {
                return Result.Success(Unit)
            }

            override suspend fun updateUserProfileData(data: ProfileUpdateData): Result<Unit> {
                return Result.Success(Unit)
            }

            override suspend fun getLoggedUser(): Result<User> {
                return Result.Success(User())
            }

        }
        loginWithEmailAndPasswordFake = LoginWithEmailAndPassword(fakeAuthRepository)
        loginWithCredentialFake = LoginWithCredential(fakeAuthRepository)
        validateEmailFake = ValidateEmail()
        validatePasswordFake = ValidatePassword()
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




}
