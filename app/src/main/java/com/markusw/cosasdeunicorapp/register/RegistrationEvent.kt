package com.markusw.cosasdeunicorapp.register

import com.markusw.cosasdeunicorapp.core.utils.UiText

sealed interface RegistrationEvent {
    data class TermsNotAccepted(val message: UiText): RegistrationEvent
    object SuccessfullyRegistration : RegistrationEvent
    data class RegistrationFailed(val reason: UiText): RegistrationEvent
}