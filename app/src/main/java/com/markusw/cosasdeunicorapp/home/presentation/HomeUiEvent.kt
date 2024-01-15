package com.markusw.cosasdeunicorapp.home.presentation

import com.markusw.cosasdeunicorapp.home.domain.model.Message
import com.markusw.cosasdeunicorapp.home.domain.model.News

sealed interface HomeUiEvent {
    data object FetchPreviousGlobalMessages : HomeUiEvent
    data object CloseSession : HomeUiEvent
    data class MessageChanged(val message: String) : HomeUiEvent
    data object SendMessageToGlobalChat : HomeUiEvent
    data class ReplyToMessage(val message: Message): HomeUiEvent
    data object ClearReplyMessage: HomeUiEvent
    data class DownloadDocument(val fileName: String): HomeUiEvent
    data object FetchPreviousNews: HomeUiEvent
    data class LikeNews(val news: News): HomeUiEvent
    data object ToggleGeneralChatNotifications: HomeUiEvent
}