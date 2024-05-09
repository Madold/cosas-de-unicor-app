package com.markusw.cosasdeunicorapp.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.markusw.cosasdeunicorapp.auth.domain.use_cases.ValidateName
import com.markusw.cosasdeunicorapp.core.DispatcherProvider
import com.markusw.cosasdeunicorapp.core.domain.ProfileUpdateData
import com.markusw.cosasdeunicorapp.core.domain.model.User
import com.markusw.cosasdeunicorapp.core.domain.use_cases.ValidateEmail
import com.markusw.cosasdeunicorapp.core.utils.Result
import com.markusw.cosasdeunicorapp.profile.domain.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
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
        viewModelScope.launch(dispatchers.io) {
            when (val result = profileRepository.onUserInfoUpdate()) {
                is Result.Error -> {
                    _uiState.update { state ->
                        state.copy(
                            profileUpdateError = result.message,
                            user = User(
                                displayName = "Error",
                                email = "Error",
                                photoUrl = "Error"
                            )
                        )
                    }
                }

                is Result.Success -> {
                    result.data?.collectLatest { latestUserInfo ->
                        _uiState.update { state ->
                            state.copy(
                                user = latestUserInfo,
                                name = latestUserInfo.displayName,
                                email = latestUserInfo.email,
                                profilePhoto = null
                            )
                        }
                    }
                }
            }
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
                        profileSaveState = AsyncOperationState.LOADING
                    )
                }

                viewModelScope.launch(dispatchers.io) {
                    val updatedData = ProfileUpdateData(
                        displayName = uiState.value.name,
                        email = uiState.value.email,
                        profilePhotoUri = uiState.value.profilePhoto
                    )

                    when (val result = profileRepository.updateProfile(updatedData)) {
                        is Result.Error -> {
                            _uiState.update { state ->
                                state.copy(
                                    profileSaveState = AsyncOperationState.ERROR,
                                    profileUpdateError = result.message
                                )
                            }
                        }

                        is Result.Success -> {
                            _uiState.update { state ->
                                state.copy(
                                    profileSaveState = AsyncOperationState.SUCCESS
                                )
                            }
                            delay(2500)
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
                        passwordResetState = AsyncOperationState.LOADING
                    )
                }

                viewModelScope.launch(dispatchers.io) {
                    when (val result =
                        profileRepository.sendPasswordResetByEmail(loggedUserEmail)) {
                        is Result.Error -> {
                            _uiState.update { state ->
                                state.copy(
                                    passwordResetState = AsyncOperationState.ERROR,
                                    passwordUpdateError = result.message
                                )
                            }
                        }

                        is Result.Success -> {
                            _uiState.update { state ->
                                state.copy(
                                    passwordResetState = AsyncOperationState.SUCCESS
                                )
                            }
                            delay(2000)
                            eventsChannel.send(ProfileViewModelEvent.PasswordResetSentSuccess)
                        }
                    }
                }
            }

            is ProfileScreenEvent.DismissProfileUpdatedDialog -> {
                _uiState.update { state ->
                    state.copy(
                        profileSaveState = AsyncOperationState.IDLE
                    )
                }
            }

            is ProfileScreenEvent.DismissPasswordResetDialog -> {
                _uiState.update { state ->
                    state.copy(
                        passwordResetState = AsyncOperationState.IDLE
                    )
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        eventsChannel.close()
    }

}