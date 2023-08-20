package com.markusw.cosasdeunicorapp.home

import com.markusw.cosasdeunicorapp.data.model.Message
import com.markusw.cosasdeunicorapp.data.model.User

data class HomeState(
    val globalChatList: List<Message> = emptyList(),
    val message: String = "",
    val currentUser: User = User(),
)
