package com.markusw.cosasdeunicorapp.login

interface AuthenticationEvent {
    object AuthSuccessful: AuthenticationEvent
    data class AuthFailed(val reason: String): AuthenticationEvent
}