package com.markusw.cosasdeunicorapp.login

sealed interface AuthenticationEvent {
    object AuthSuccessful: AuthenticationEvent
    data class AuthFailed(val reason: String): AuthenticationEvent
}