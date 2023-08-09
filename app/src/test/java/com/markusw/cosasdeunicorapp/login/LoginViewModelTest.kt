package com.markusw.cosasdeunicorapp.login

import com.markusw.cosasdeunicorapp.core.utils.Resource
import com.markusw.cosasdeunicorapp.domain.services.AuthService
import com.markusw.cosasdeunicorapp.domain.use_cases.ValidateEmail
import com.markusw.cosasdeunicorapp.domain.use_cases.ValidatePassword
import org.junit.Before
import org.junit.Test


class LoginViewModelTest {

    private lateinit var viewModel: LoginViewModel
    private lateinit var validateEmail: ValidateEmail
    private lateinit var validatePassword: ValidatePassword
    private lateinit var authService: AuthService

    @Before
    fun setUp() {
        authService = object: AuthService {
            override suspend fun authenticate(email: String, password: String): Resource<Unit> {
                return Resource.Success(Unit)
            }

            override suspend fun register(email: String, password: String): Resource<Unit> {
                return Resource.Success(Unit)
            }
        }
        validateEmail = ValidateEmail()
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

}