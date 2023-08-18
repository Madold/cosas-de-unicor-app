package com.markusw.cosasdeunicorapp.home

import android.support.annotation.DrawableRes
import com.markusw.cosasdeunicorapp.R

sealed class HomeScreens(
    val route: String,
    val label: String,
    @DrawableRes val icon: Int,
) {
    object Home : HomeScreens(
        route =  "home",
        label = "Inicio",
        icon = R.drawable.ic_home
    )
    object Chat : HomeScreens(
        route = "chat",
        label = "Chat",
        icon = R.drawable.ic_chat
    )
    object News : HomeScreens(
        route = "news",
        label = "Muro",
        icon = R.drawable.ic_news
    )
    object Documents : HomeScreens(
        route = "documents",
        label = "Docs",
        icon = R.drawable.ic_documents
    )
    object More : HomeScreens(
        route = "more",
        label = "Más",
        icon = R.drawable.ic_more_horizontal
    )
}
