package com.markusw.cosasdeunicorapp.core.presentation

sealed class Screens(
    val route: String,
) {

    data object Home : Screens(
        route = "home",
    )

    data object Profile : Screens(
        route = "profile",
    )

    data object EditProfile : Screens(
        route = "profile/edit",
    )

    data object ResetPassword : Screens(
        route = "profile/reset_password",
    )

    data object Tabulator : Screens(
        route = "tabulator",
    )
}