package com.markusw.cosasdeunicorapp.home.presentation.more

import android.support.annotation.DrawableRes
import com.markusw.cosasdeunicorapp.R

sealed class FeatureDestination (
    val route: String,
    val label: String,
    @DrawableRes val iconSrc: Int
){

    companion object {
        fun values() = listOf(Tabulator, Map, RateTeachers)
    }

    data object Tabulator : FeatureDestination(
        route = "tabulator",
        label = "Tabulador",
        iconSrc = R.drawable.ic_excel_mono
    )

    data object Map: FeatureDestination(
        route = "home_map",
        label = "Mapa de la Universidad",
        iconSrc = R.drawable.ic_map
    )

    data object RateTeachers: FeatureDestination(
        route = "home_rate_teachers",
        label = "Calificar Profesores",
        iconSrc = R.drawable.ic_teacher
    )

}