package com.markusw.cosasdeunicorapp.auth.presentation.register

import com.markusw.cosasdeunicorapp.core.presentation.UiText

sealed interface RegistrationEvent {
    data class TermsNotAccepted(val message: UiText): RegistrationEvent
    object SuccessfullyRegistration : RegistrationEvent
    data class RegistrationFailed(val reason: UiText): RegistrationEvent
}