package com.markusw.cosasdeunicorapp.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.markusw.cosasdeunicorapp.core.utils.Resource
import com.markusw.cosasdeunicorapp.domain.services.AuthService
import com.markusw.cosasdeunicorapp.domain.use_cases.ValidateEmail
import com.markusw.cosasdeunicorapp.domain.use_cases.ValidateName
import com.markusw.cosasdeunicorapp.domain.use_cases.ValidatePassword
import com.markusw.cosasdeunicorapp.domain.use_cases.ValidateRepeatedPassword
import com.markusw.cosasdeunicorapp.domain.use_cases.ValidateTerms
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authService: AuthService,
    private val validateName: ValidateName,
    private val validateEmail: ValidateEmail,
    private val validatePassword: ValidatePassword,
    private val validateRepeatedPassword: ValidateRepeatedPassword,
    private val validateTerms: ValidateTerms
) : ViewModel() {

    private var _uiState = MutableStateFlow(RegisterState())
    val uiState = _uiState.asStateFlow()
    private val registrationEventChannel = Channel<RegistrationEvent>()
    val registrationEvents = registrationEventChannel.receiveAsFlow()

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
        viewModelScope.launch(Dispatchers.IO) {
            val nameValidationResult = validateName(uiState.value.name)
            val emailValidationResult = validateEmail(uiState.value.email)
            val passwordValidationResult = validatePassword(uiState.value.password)
            val repeatedPasswordValidationResult =
                validateRepeatedPassword(uiState.value.password, uiState.value.repeatedPassword)
            val termsValidationResult = validateTerms(uiState.value.isTermsAccepted)

            val isAnyError = listOf(
                nameValidationResult,
                emailValidationResult,
                passwordValidationResult,
                repeatedPasswordValidationResult,
                termsValidationResult
            ).any { !it.successful }

            if (isAnyError) {
                _uiState.update {
                    it.copy(
                        nameError = nameValidationResult.errorMessage,
                        emailError = emailValidationResult.errorMessage,
                        passwordError = passwordValidationResult.errorMessage,
                        repeatedPasswordError = repeatedPasswordValidationResult.errorMessage,
                        termsError = termsValidationResult.errorMessage
                    )
                }


                termsValidationResult.errorMessage?.let {
                    registrationEventChannel.send(RegistrationEvent.TermsNotAccepted(it))
                }


                return@launch
            }

            val email = uiState.value.email
            val password = uiState.value.password


            _uiState.update {
                it.copy(
                    isLoading = true,
                )
            }
            when (val registrationResult = authService.register(email, password)) {
                is Resource.Error -> {
                    registrationEventChannel.send(
                        RegistrationEvent.RegistrationFailed(
                            reason = registrationResult.message!!
                        )
                    )
                }

                is Resource.Success -> {
                    registrationEventChannel.send(RegistrationEvent.SuccessfullyRegistration)
                }
            }
            _uiState.update {
                it.copy(
                    isLoading = false,
                )
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        registrationEventChannel.close()
    }

}