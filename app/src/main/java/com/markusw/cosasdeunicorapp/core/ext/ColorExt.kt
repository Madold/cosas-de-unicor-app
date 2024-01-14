package com.markusw.cosasdeunicorapp.core.ext

import androidx.compose.ui.graphics.Color

fun Color.invert(): Color {
    return Color(
        red = 1.0f - this.red,
        green = 1.0f - this.green,
        blue = 1.0f - this.blue,
        alpha = this.alpha
    )
}