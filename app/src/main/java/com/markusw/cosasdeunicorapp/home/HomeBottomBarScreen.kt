package com.markusw.cosasdeunicorapp.home

import android.support.annotation.DrawableRes
import com.markusw.cosasdeunicorapp.R

sealed class HomeBottomBarScreen(
    val title: String,
    @DrawableRes val icon: Int
) {

    object Chat : HomeBottomBarScreen(
        title = "Chat",
        icon = R.drawable.ic_chat
    )

    object News: HomeBottomBarScreen(
        title = "Muro",
        icon = R.drawable.ic_news
    )

    object Home: HomeBottomBarScreen(
        title = "Inicio",
        icon = R.drawable.ic_home
    )

    object Documents: HomeBottomBarScreen(
        title = "Docs",
        icon = R.drawable.ic_documents
    )

    object More: HomeBottomBarScreen(
        title = "Más",
        icon = R.drawable.ic_more_horizontal
    )

}
