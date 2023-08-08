package com.markusw.cosasdeunicorapp.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.markusw.cosasdeunicorapp.core.utils.Resource
import com.markusw.cosasdeunicorapp.domain.services.AuthService
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val authService: AuthService
): ViewModel() {

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
        viewModelScope.launch {
            val email = _uiState.value.email
            val password = _uiState.value.password

            when (val result = authService.authenticate(email, password)) {
                is Resource.Error -> {
                    authenticationEventChannel.send(AuthenticationEvent.AuthFailed(reason = result.message!!))
                }
                is Resource.Success -> {
                    authenticationEventChannel.send(AuthenticationEvent.AuthSuccessful)
                }
            }
        }
    }

}