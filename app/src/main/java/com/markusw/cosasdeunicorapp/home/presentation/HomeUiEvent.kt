package com.markusw.cosasdeunicorapp.home.presentation

sealed interface HomeUiEvent {
    object FetchPreviousGlobalMessages : HomeUiEvent
    object CloseSession : HomeUiEvent
    data class MessageChanged(val message: String) : HomeUiEvent
    object SendMessageToGlobalChat : HomeUiEvent
}