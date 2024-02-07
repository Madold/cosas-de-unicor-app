package com.markusw.cosasdeunicorapp.core.domain.model

data class LocalSettings(
    val isGeneralChatNotificationsEnabled: Boolean = true,
    val isNewsNotificationsEnabled: Boolean = true,
    val isDarkModeEnabled: Boolean = false,
    val isAppSoundsEnabled: Boolean = true
)