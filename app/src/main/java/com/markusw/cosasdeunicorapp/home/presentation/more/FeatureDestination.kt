package com.markusw.cosasdeunicorapp.home.presentation.more

import android.support.annotation.DrawableRes
import com.markusw.cosasdeunicorapp.R

sealed class FeatureDestination (
    val route: String,
    val label: String,
    @DrawableRes val iconSrc: Int
){

    companion object {
        fun values() = listOf(Tabulator, RateTeachers)
    }

    data object Tabulator : FeatureDestination(
        route = "tabulator",
        label = "Tabulador",
        iconSrc = R.drawable.ic_excel_mono
    )

    data object RateTeachers: FeatureDestination(
        route = "teacher_rating",
        label = "Calificar Profesores",
        iconSrc = R.drawable.ic_teacher
    )

}