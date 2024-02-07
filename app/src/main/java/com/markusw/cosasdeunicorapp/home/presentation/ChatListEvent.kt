package com.markusw.cosasdeunicorapp.home.presentation

sealed interface ChatListEvent {
    object MessageAdded : ChatListEvent
}