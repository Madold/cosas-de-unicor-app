package com.markusw.cosasdeunicorapp.register

sealed interface RegistrationEvent {
    object Success : RegistrationEvent
    data class RegistrationFailed(val reason: String): RegistrationEvent
}