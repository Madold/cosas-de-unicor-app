package com.markusw.cosasdeunicorapp.profile.presentation

sealed interface ProfileViewModelEvent {
    data object PasswordResetSentSuccess : ProfileViewModelEvent
    data object ProfileUpdatedSuccess : ProfileViewModelEvent
    data object ProfileUpdatedError : ProfileViewModelEvent
}