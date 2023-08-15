package com.markusw.cosasdeunicorapp.home.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markusw.cosasdeunicorapp.home.HomeBottomBarScreen
import com.markusw.cosasdeunicorapp.ui.theme.home_bottom_bar_background

@Composable
fun BottomNavigationBar(
    modifier: Modifier = Modifier,
    onScreenClicked: (HomeBottomBarScreen) -> Unit,
) {

    val screens = listOf(
        HomeBottomBarScreen.Chat,
        HomeBottomBarScreen.News,
        HomeBottomBarScreen.Home,
        HomeBottomBarScreen.Documents,
        HomeBottomBarScreen.More,
    )

    var selectedScreen: HomeBottomBarScreen by remember {
        mutableStateOf(HomeBottomBarScreen.Home)
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(color = home_bottom_bar_background),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            screens.forEach { screen ->
                BottomNavigationBarItem(
                    screen = screen,
                    selected = screen == selectedScreen,
                    onClick = {
                        selectedScreen = it
                        onScreenClicked(it)
                    }
                )
            }
        }
    }

}