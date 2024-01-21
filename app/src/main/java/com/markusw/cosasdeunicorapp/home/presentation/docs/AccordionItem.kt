package com.markusw.cosasdeunicorapp.home.presentation.docs

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markusw.cosasdeunicorapp.core.utils.TextUtils

@Composable
fun AccordionItem(
    label: @Composable () -> Unit,
    onItemClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit,
) {

    NavigationDrawerItem(
        label = label,
        selected = false,
        onClick = onItemClick,
        modifier = modifier,
        icon = icon,
        shape = RoundedCornerShape(8.dp)
    )


}