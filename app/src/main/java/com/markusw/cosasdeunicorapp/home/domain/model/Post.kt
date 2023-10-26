package com.markusw.cosasdeunicorapp.home.domain.model

data class Post(
    val title: String,
    val body: String,
    val imageURL: String,
    val timestamp: Long,
)
