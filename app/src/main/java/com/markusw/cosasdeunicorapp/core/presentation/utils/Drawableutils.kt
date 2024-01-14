package com.markusw.cosasdeunicorapp.core.presentation.utils

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.ui.graphics.Color
import androidx.palette.graphics.Palette

fun calculateDominantColor(drawable: Drawable, defaultColorValue: Int): Color {

    val bitmap = (drawable as BitmapDrawable)
        .bitmap
        .copy(
            Bitmap.Config.ARGB_8888,
            true
        )

    val colorValue =  Palette
        .from(bitmap)
        .generate()
        .dominantSwatch
        ?.rgb

    return Color(colorValue ?: defaultColorValue)

}