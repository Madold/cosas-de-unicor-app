package com.markusw.cosasdeunicorapp.home

import com.markusw.cosasdeunicorapp.data.model.Message

data class HomeState(
    val globalChatList: List<Message> = emptyList(),
)
