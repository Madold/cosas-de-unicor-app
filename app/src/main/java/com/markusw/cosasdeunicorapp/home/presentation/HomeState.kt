package com.markusw.cosasdeunicorapp.home.presentation

import com.markusw.cosasdeunicorapp.home.domain.model.Message
import com.markusw.cosasdeunicorapp.core.domain.model.User

data class HomeState(
    val globalChatList: List<Message> = emptyList(),
    val message: String = "",
    val currentUser: User = User(),
    val isFetchingPreviousGlobalMessages: Boolean = false,
    val repliedMessage: Message? = null,
    val isDownloadingDocument: Boolean = false
)
