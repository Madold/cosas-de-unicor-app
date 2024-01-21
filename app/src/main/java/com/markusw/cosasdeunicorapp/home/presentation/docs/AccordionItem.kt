package com.markusw.cosasdeunicorapp.home.presentation.docs

import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.markusw.cosasdeunicorapp.core.utils.TextUtils

@Composable
fun AccordionItem(
    label: @Composable () -> Unit,
    onItemClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit,
) {

    /*
    Text(text = title, modifier = modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(8.dp))
        .clickable(onClick = onItemClick)
        .padding(vertical = 16.dp, horizontal = 16.dp)

    )*/


    NavigationDrawerItem(
        label = label,
        selected = false,
        onClick = onItemClick,
        modifier = modifier,
        icon = icon
    )


}