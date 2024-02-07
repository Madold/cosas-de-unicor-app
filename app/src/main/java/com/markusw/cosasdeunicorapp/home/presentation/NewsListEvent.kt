package com.markusw.cosasdeunicorapp.home.presentation

sealed interface NewsListEvent {
    object NewsAdded : NewsListEvent
}