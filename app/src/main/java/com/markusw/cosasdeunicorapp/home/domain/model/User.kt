package com.markusw.cosasdeunicorapp.home.domain.model

data class User(
    val displayName: String = "",
    val email: String = "",
    val photoUrl: String = "",
    val uid: String = "",
    val messagingToken: String = ""
)