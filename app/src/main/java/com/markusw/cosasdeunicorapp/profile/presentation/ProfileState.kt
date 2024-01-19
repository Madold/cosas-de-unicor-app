package com.markusw.cosasdeunicorapp.profile.presentation

import com.markusw.cosasdeunicorapp.core.domain.model.User
import com.markusw.cosasdeunicorapp.core.presentation.UiText

data class ProfileState(
    val user: User = User(),
    val isLoading: Boolean = false,
    val name: String = "",
    val nameError: UiText? = null,
    val email: String = "",
    val emailError: UiText? = null,
    val profilePhoto: String? = null,
    val passwordResetState: PasswordResetState = PasswordResetState.IDLE,
)

enum class PasswordResetState {
    IDLE,
    LOADING,
    SUCCESS,
    ERROR
}