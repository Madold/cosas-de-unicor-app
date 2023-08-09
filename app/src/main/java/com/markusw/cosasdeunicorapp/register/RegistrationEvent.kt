package com.markusw.cosasdeunicorapp.register

sealed interface RegistrationEvent {
    object SuccessfullyRegistration : RegistrationEvent
    data class RegistrationFailed(val reason: String): RegistrationEvent
}