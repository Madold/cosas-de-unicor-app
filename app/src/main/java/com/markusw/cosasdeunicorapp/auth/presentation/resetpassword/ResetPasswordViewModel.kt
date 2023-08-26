package com.markusw.cosasdeunicorapp.auth.presentation.resetpassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.markusw.cosasdeunicorapp.core.DispatcherProvider
import com.markusw.cosasdeunicorapp.core.utils.Resource
import com.markusw.cosasdeunicorapp.core.domain.AuthService
import com.markusw.cosasdeunicorapp.core.domain.use_cases.ValidateEmail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResetPasswordViewModel @Inject constructor(
    private val validateEmail: ValidateEmail,
    private val authService: AuthService,
    private val dispatchers: DispatcherProvider
) : ViewModel() {

    private var _uiState = MutableStateFlow(ResetPasswordState())
    val uiState = _uiState.asStateFlow()
    private val resetPasswordEventsChannel = Channel<ResetPasswordEvents>()
    val resetPasswordEvents = resetPasswordEventsChannel.receiveAsFlow()

    fun onEmailChanged(email: String) {
        _uiState.update { it.copy(email = email) }
    }

    fun onContinue() {

        val emailValidationResult = validateEmail(uiState.value.email)

        if (!emailValidationResult.successful) {
            _uiState.update {
                it.copy(
                    emailError = emailValidationResult.errorMessage
                )
            }
            return
        }

        resetEmailError()

        viewModelScope.launch(dispatchers.io) {
            _uiState.update { it.copy(isLoading = true) }
            when (val result = authService.sendPasswordResetByEmail(uiState.value.email)) {
                is Resource.Error -> {
                    resetPasswordEventsChannel.send(ResetPasswordEvents.EmailSentError(result.message!!))
                }
                is Resource.Success -> {
                    resetPasswordEventsChannel.send(ResetPasswordEvents.EmailSentSuccessfully)
                }
            }
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    private fun resetEmailError() {
        _uiState.update { it.copy(emailError = null) }
    }

}