package com.markusw.cosasdeunicorapp.register

sealed interface RegistrationEvent {
    data class TermsNotAccepted(val message: String): RegistrationEvent
    object SuccessfullyRegistration : RegistrationEvent
    data class RegistrationFailed(val reason: String): RegistrationEvent
}