package com.markusw.cosasdeunicorapp.auth.presentation.resetpassword

import com.markusw.cosasdeunicorapp.core.presentation.UiText

sealed interface ResetPasswordEvents {
    object EmailSentSuccessfully : ResetPasswordEvents
    data class EmailSentError(val reason: UiText) : ResetPasswordEvents
}