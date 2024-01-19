package com.markusw.cosasdeunicorapp.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.markusw.cosasdeunicorapp.auth.domain.use_cases.ValidateName
import com.markusw.cosasdeunicorapp.core.DispatcherProvider
import com.markusw.cosasdeunicorapp.core.domain.ProfileUpdateData
import com.markusw.cosasdeunicorapp.core.domain.use_cases.ValidateEmail
import com.markusw.cosasdeunicorapp.core.utils.Result
import com.markusw.cosasdeunicorapp.profile.domain.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val dispatchers: DispatcherProvider,
    private val profileRepository: ProfileRepository,
    private val validateName: ValidateName,
    private val validateEmail: ValidateEmail,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileState())
    val uiState = _uiState.asStateFlow()
    private val eventsChannel = Channel<ProfileViewModelEvent>()
    val events = eventsChannel.receiveAsFlow()

    init {
        _uiState.update { state ->
            val loggedUser = profileRepository.getLoggedUser()
            state.copy(
                user = loggedUser,
                name = loggedUser.displayName,
                email = loggedUser.email,
            )
        }
    }

    fun onEvent(event: ProfileScreenEvent) {
        when (event) {
            is ProfileScreenEvent.ChangeEmail -> {
                _uiState.update { it.copy(email = event.email) }
            }

            is ProfileScreenEvent.ChangeName -> {
                _uiState.update { it.copy(name = event.name) }
            }

            is ProfileScreenEvent.ChangeProfilePhoto -> {
                _uiState.update { it.copy(profilePhoto = event.profilePhoto) }
            }

            is ProfileScreenEvent.SaveChanges -> {

                val nameValidationResult = validateName(uiState.value.name)
                val emailValidationResult = validateEmail(uiState.value.email)
                val isAnyError = listOf(
                    nameValidationResult,
                    emailValidationResult
                ).any { !it.successful }

                if (isAnyError) {
                    _uiState.update {
                        it.copy(
                            nameError = nameValidationResult.errorMessage,
                            emailError = emailValidationResult.errorMessage
                        )
                    }
                    return
                }


                _uiState.update { state ->
                    state.copy(
                        isLoading = true,
                        nameError = null,
                        emailError = null,
                    )
                }

                viewModelScope.launch(dispatchers.io) {
                    val updatedData = ProfileUpdateData(
                        displayName = uiState.value.name,
                        email = uiState.value.email,
                        profilePhotoUri = uiState.value.profilePhoto
                    )

                    when (profileRepository.updateProfile(updatedData)) {
                        is Result.Error -> {
                            _uiState.update { state ->
                                state.copy(
                                    isLoading = false,
                                )
                            }
                        }

                        is Result.Success -> {
                            eventsChannel.send(ProfileViewModelEvent.ProfileUpdatedSuccess)
                        }
                    }
                    _uiState.update { state ->
                        state.copy(
                            isLoading = false,
                        )
                    }
                }
            }

            is ProfileScreenEvent.SendPasswordResetByEmail -> {
                val loggedUserEmail = uiState.value.user.email
                _uiState.update { state ->
                    state.copy(
                        passwordResetState = PasswordResetState.LOADING
                    )
                }

                viewModelScope.launch(dispatchers.io) {
                    when (profileRepository.sendPasswordResetByEmail(loggedUserEmail)) {
                        is Result.Error -> {
                            _uiState.update { state ->
                                state.copy(
                                    passwordResetState = PasswordResetState.ERROR
                                )
                            }
                        }

                        is Result.Success -> {
                            _uiState.update { state ->
                                state.copy(
                                    passwordResetState = PasswordResetState.SUCCESS
                                )
                            }
                            delay(2000)
                            eventsChannel.send(ProfileViewModelEvent.PasswordResetSentSuccess)
                        }
                    }
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        eventsChannel.close()
    }

}