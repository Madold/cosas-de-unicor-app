package com.markusw.cosasdeunicorapp.home.domain.model

data class Message(
    val content: String = "",
    val sender: User = User(),
    val timestamp: Long = 0L
)
