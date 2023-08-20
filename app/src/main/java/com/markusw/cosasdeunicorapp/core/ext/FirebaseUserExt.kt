package com.markusw.cosasdeunicorapp.core.ext

import com.google.firebase.auth.FirebaseUser
import com.markusw.cosasdeunicorapp.data.model.User

fun FirebaseUser.toUserModel(): User {
    return User(
        this.displayName,
        this.email,
        this.photoUrl.toString(),
        this.uid
    )
}