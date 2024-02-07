package com.markusw.cosasdeunicorapp.home.presentation.chat.composables

import android.support.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.markusw.cosasdeunicorapp.ui.theme.md_theme_light_primary

@Composable
fun RoundedIconButton(
    @DrawableRes icon: Int,
    modifier: Modifier = Modifier,
    iconTint: Color = Color.White,
    onClick: () -> Unit = {},
    enabled: Boolean = true,
    backgroundColor: Color = md_theme_light_primary,
    iconContentDescription: String? = null
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .run {
                if (enabled) clickable(onClick = onClick)
                else this
            }
            .background(backgroundColor)
            .padding(all = 10.dp)
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = iconContentDescription,
            tint = iconTint
        )
    }
}