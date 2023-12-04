package com.markusw.cosasdeunicorapp.core.ext

import com.google.firebase.auth.FirebaseUser
import com.markusw.cosasdeunicorapp.core.domain.model.User

fun FirebaseUser.toDomainModel(): User {
    return User(
        this.displayName!!,
        this.email!!,
        this.photoUrl.toString(),
        this.uid
    )
}