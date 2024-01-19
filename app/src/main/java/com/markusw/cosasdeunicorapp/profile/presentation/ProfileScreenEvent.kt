package com.markusw.cosasdeunicorapp.profile.presentation

sealed interface ProfileScreenEvent {
    data class ChangeName(val name: String): ProfileScreenEvent
    data class ChangeEmail(val email: String): ProfileScreenEvent
    data class ChangeProfilePhoto(val profilePhoto: String): ProfileScreenEvent
    data object SaveChanges: ProfileScreenEvent
    data object SendPasswordResetByEmail: ProfileScreenEvent
}