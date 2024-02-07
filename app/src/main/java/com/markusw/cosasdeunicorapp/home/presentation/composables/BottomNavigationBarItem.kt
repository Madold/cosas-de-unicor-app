package com.markusw.cosasdeunicorapp.home.presentation.composables

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
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
        Icon(
            painter = painterResource(id = icon),
            contentDescription = label,
            tint = emphasisColor
        )
    }
}