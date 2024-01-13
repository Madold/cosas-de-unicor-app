package com.markusw.cosasdeunicorapp.home.domain.model

import com.markusw.cosasdeunicorapp.core.domain.model.User

data class News(
    val title: String = "",
    val content: String = "",
    val coverUrl: String = "",
    val timestamp: Long = 0L,
    val likedBy: List<User> = listOf(),
    val id: String? = null
)
