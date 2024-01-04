package com.markusw.cosasdeunicorapp.home.presentation

import com.markusw.cosasdeunicorapp.home.domain.model.Message

sealed interface HomeUiEvent {
    object FetchPreviousGlobalMessages : HomeUiEvent
    object CloseSession : HomeUiEvent
    data class MessageChanged(val message: String) : HomeUiEvent
    object SendMessageToGlobalChat : HomeUiEvent
    data class ReplyToMessage(val message: Message): HomeUiEvent
    object ClearReplyMessage: HomeUiEvent
    data class DownloadDocument(val fileName: String): HomeUiEvent
    object FetchPreviousNews: HomeUiEvent
}