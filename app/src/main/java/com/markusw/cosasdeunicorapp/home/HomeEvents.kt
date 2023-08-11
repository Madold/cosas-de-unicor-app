package com.markusw.cosasdeunicorapp.home

sealed interface HomeEvents {
    object LogoutSuccessful: HomeEvents
}