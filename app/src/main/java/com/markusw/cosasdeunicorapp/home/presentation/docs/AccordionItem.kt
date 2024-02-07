package com.markusw.cosasdeunicorapp.home.presentation.docs

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemColors
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AccordionItem(
    label: @Composable () -> Unit,
    onItemClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit,
    colors: NavigationDrawerItemColors = NavigationDrawerItemDefaults.colors(),
) {

    NavigationDrawerItem(
        label = label,
        selected = false,
        onClick = onItemClick,
        modifier = modifier,
        icon = icon,
        shape = RoundedCornerShape(8.dp),
        colors = colors,
    )


}