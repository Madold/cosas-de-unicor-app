@file:OptIn(ExperimentalMaterial3Api::class)

package com.markusw.cosasdeunicorapp.core.presentation

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import com.markusw.cosasdeunicorapp.ui.theme.home_bottom_bar_background

@Composable
fun AppTopBar(
    title: @Composable () -> Unit,
    actions: @Composable RowScope.() -> Unit = {},
    navigationIcon: @Composable () -> Unit = {},
) {

    CenterAlignedTopAppBar(
        title = title,
        actions = actions,
        navigationIcon = navigationIcon,
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = home_bottom_bar_background
        )
    )

}