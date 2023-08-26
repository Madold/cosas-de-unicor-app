package com.markusw.cosasdeunicorapp.home.presentation

sealed interface HomeEvents {
    object LogoutSuccessful: HomeEvents
}