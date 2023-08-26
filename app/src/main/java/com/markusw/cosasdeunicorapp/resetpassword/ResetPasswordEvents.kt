package com.markusw.cosasdeunicorapp.resetpassword

import com.markusw.cosasdeunicorapp.core.utils.UiText

sealed interface ResetPasswordEvents {
    object EmailSentSuccessfully : ResetPasswordEvents
    data class EmailSentError(val reason: UiText) : ResetPasswordEvents
}