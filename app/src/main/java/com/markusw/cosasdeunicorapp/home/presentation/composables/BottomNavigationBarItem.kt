package com.markusw.cosasdeunicorapp.home.presentation.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import com.markusw.cosasdeunicorapp.ui.theme.md_theme_light_primary

@Composable
fun BottomNavigationBarItem(
    label: String,
    icon: Int,
    selected: Boolean = false,
    onClick: () -> Unit = {},
) {

    val emphasisColor by remember(key1 = selected) {
        derivedStateOf {
            if (selected) md_theme_light_primary else Color.White
        }
    }

    IconButton(onClick = onClick) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = label,
                tint = emphasisColor
            )
            AnimatedVisibility(visible = selected) {
                Text(text = label, color = emphasisColor, fontSize = 14.sp)
            }
        }
    }
}