package com.markusw.cosasdeunicorapp.auth.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthCredential
import com.markusw.cosasdeunicorapp.auth.domain.use_cases.LoginWithCredential
import com.markusw.cosasdeunicorapp.auth.domain.use_cases.LoginWithEmailAndPassword
import com.markusw.cosasdeunicorapp.core.DispatcherProvider
import com.markusw.cosasdeunicorapp.core.domain.use_cases.ValidateEmail
import com.markusw.cosasdeunicorapp.core.domain.use_cases.ValidatePassword
import com.markusw.cosasdeunicorapp.core.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginWithEmailAndPassword: LoginWithEmailAndPassword,
    private val loginWithCredential: LoginWithCredential,
    private val validateEmail: ValidateEmail,
    private val validatePassword: ValidatePassword,
    private val dispatchers: DispatcherProvider
) : ViewModel() {

    private var _uiState = MutableStateFlow(LoginState())
    val uiState = _uiState.asStateFlow()
    private val authenticationEventChannel = Channel<AuthenticationEvent>()
    val authenticationEvents = authenticationEventChannel.receiveAsFlow()

    fun onEmailChanged(email: String) {
        _uiState.update { it.copy(email = email) }
    }

    fun onPasswordChanged(password: String) {
        _uiState.update { it.copy(password = password) }
    }

    fun onLogin() {

        val emailValidationResult = validateEmail(uiState.value.email)
        val passwordValidationResult = validatePassword(uiState.value.password)
        val isAnyError = listOf(
            emailValidationResult,
            passwordValidationResult
        ).any { !it.successful }

        if (isAnyError) {
            _uiState.update {
                it.copy(
                    emailError = emailValidationResult.errorMessage,
                    passwordError = passwordValidationResult.errorMessage
                )
            }
            return
        }

        resetErrors()

        viewModelScope.launch(dispatchers.io) {
            _uiState.update { it.copy(isLoading = true) }
            val email = uiState.value.email
            val password = uiState.value.password
            when (val authResult = loginWithEmailAndPassword(email, password)) {
                is Result.Error -> {
                    authenticationEventChannel.send(AuthenticationEvent.AuthFailed(reason = authResult.message!!))
                }

                is Result.Success -> {
                    authenticationEventChannel.send(AuthenticationEvent.AuthSuccessful)
                }
            }
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun onGoogleSignInStarted() {
        _uiState.update { it.copy(isLoading = true) }
    }

    fun onGoogleSignInFinished() {
        _uiState.update { it.copy(isLoading = false) }
    }

    fun onGoogleSignInResult(googleCredential: AuthCredential) {
        viewModelScope.launch(dispatchers.io) {
            when (val authResult = loginWithCredential(googleCredential)) {
                is Result.Error -> {
                    authenticationEventChannel.send(AuthenticationEvent.AuthFailed(reason = authResult.message!!))
                }
                is Result.Success -> {
                    authenticationEventChannel.send(AuthenticationEvent.AuthSuccessful)
                }
            }
        }
    }

    private fun resetErrors() {
        _uiState.update {
            it.copy(
                emailError = null,
                passwordError = null
            )
        }
    }

    fun onGoogleSignInCanceled() {
        _uiState.update { it.copy(isLoading = false) }
    }

    override fun onCleared() {
        super.onCleared()
        authenticationEventChannel.close()
    }

}