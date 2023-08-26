package com.markusw.cosasdeunicorapp.login

import com.markusw.cosasdeunicorapp.core.utils.UiText

sealed interface AuthenticationEvent {
    object AuthSuccessful: AuthenticationEvent
    data class AuthFailed(val reason: UiText): AuthenticationEvent
}