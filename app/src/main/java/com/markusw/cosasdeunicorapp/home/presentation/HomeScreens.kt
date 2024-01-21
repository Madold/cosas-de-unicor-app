package com.markusw.cosasdeunicorapp.home.presentation

import android.support.annotation.DrawableRes
import com.markusw.cosasdeunicorapp.R

sealed class HomeScreens(
    val route: String,
    val label: String,
    @DrawableRes val icon: Int,
) {
    data object Home : HomeScreens(
        route =  "home",
        label = "Inicio",
        icon = R.drawable.ic_home
    )
    data object Chat : HomeScreens(
        route = "chat",
        label = "Chat",
        icon = R.drawable.ic_chat
    )
    data object News : HomeScreens(
        route = "news",
        label = "Muro",
        icon = R.drawable.ic_news
    )
    data object Documents : HomeScreens(
        route = "documents",
        label = "Docs",
        icon = R.drawable.ic_documents
    )
    data object More : HomeScreens(
        route = "more",
        label = "Más",
        icon = R.drawable.ic_more_horizontal
    )
}
