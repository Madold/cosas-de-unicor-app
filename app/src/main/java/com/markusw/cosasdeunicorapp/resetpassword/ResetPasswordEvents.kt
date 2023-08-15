package com.markusw.cosasdeunicorapp.resetpassword

sealed interface ResetPasswordEvents {
    object EmailSentSuccessfully : ResetPasswordEvents
    data class EmailSentError(val reason: String) : ResetPasswordEvents
}