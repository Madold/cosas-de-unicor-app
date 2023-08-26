package com.markusw.cosasdeunicorapp.auth.presentation.login

import com.markusw.cosasdeunicorapp.core.presentation.UiText

sealed interface AuthenticationEvent {
    object AuthSuccessful: AuthenticationEvent
    data class AuthFailed(val reason: UiText): AuthenticationEvent
}