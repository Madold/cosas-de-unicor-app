package com.markusw.cosasdeunicorapp.home.presentation

import com.markusw.cosasdeunicorapp.home.domain.model.Message
import com.markusw.cosasdeunicorapp.core.domain.model.User
import com.markusw.cosasdeunicorapp.home.domain.model.News

data class HomeState(
    val globalChatList: List<Message> = emptyList(),
    val message: String = "",
    val currentUser: User = User(),
    val isFetchingPreviousGlobalMessages: Boolean = false,
    val repliedMessage: Message? = null,
    val isDownloadingDocument: Boolean = false,
    val isFetchingPreviousNews: Boolean = false,
    val newsList: List<News> = emptyList(),
    val usersCount: Int = 0,
)
