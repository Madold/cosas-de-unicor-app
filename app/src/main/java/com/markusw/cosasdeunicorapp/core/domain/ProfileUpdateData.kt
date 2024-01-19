package com.markusw.cosasdeunicorapp.core.domain

data class ProfileUpdateData(
    val displayName: String? = null,
    val email: String? = null,
    val profilePhotoUri: String? = null,
)
