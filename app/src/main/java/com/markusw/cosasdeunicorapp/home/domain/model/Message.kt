package com.markusw.cosasdeunicorapp.home.domain.model

data class Message(
    val content: MessageContent = MessageContent(text = "Example message"),
    val sender: User = User(),
    val timestamp: Long = 0L
)

data class MessageContent(
    val text: String = "",
    val type: MessageType = MessageType.PlainText,
    val replyTo: Message? = null,
    val imageUrl: String? = null
)