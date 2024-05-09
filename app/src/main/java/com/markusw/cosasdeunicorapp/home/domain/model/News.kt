package com.markusw.cosasdeunicorapp.home.domain.model

data class News(
    val title: String = "",
    val content: String = "",
    val coverUrl: String = "",
    val timestamp: Long = 0L,
    val likedBy: List<String> = listOf(),
    val id: String? = null
)
