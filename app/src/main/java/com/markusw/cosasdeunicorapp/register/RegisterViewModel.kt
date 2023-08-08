package com.markusw.cosasdeunicorapp.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.markusw.cosasdeunicorapp.core.utils.Resource
import com.markusw.cosasdeunicorapp.domain.services.AuthService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authService: AuthService
) : ViewModel() {

    private var _uiState = MutableStateFlow(RegisterState())
    val uiState = _uiState.asStateFlow()

    fun onNameChanged(name: String) {
        _uiState.update { it.copy(name = name) }
    }

    fun onEmailChanged(email: String) {
        _uiState.update { it.copy(email = email) }
    }

    fun onPasswordChanged(password: String) {
        _uiState.update { it.copy(password = password) }
    }

    fun onRepeatedPasswordChanged(repeatedPassword: String) {
        _uiState.update { it.copy(repeatedPassword = repeatedPassword) }
    }

    fun onTermsChecked(isChecked: Boolean) {
        _uiState.update { it.copy(isTermsAccepted = isChecked) }
    }

    fun onRegister() {
        viewModelScope.launch {
            val email = _uiState.value.email
            val password = _uiState.value.password

            when (val registrationResult = authService.register(email, password)) {
                is Resource.Error -> {

                }
                is Resource.Success -> {

                }
            }

        }
    }

}