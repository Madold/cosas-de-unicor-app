package com.markusw.cosasdeunicorapp.home.presentation.composables

import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.exyte.animatednavbar.AnimatedNavigationBar
import com.exyte.animatednavbar.animation.balltrajectory.Straight
import com.exyte.animatednavbar.animation.indendshape.Height
import com.markusw.cosasdeunicorapp.home.presentation.HomeScreens
import com.markusw.cosasdeunicorapp.home.presentation.HomeState
import com.markusw.cosasdeunicorapp.home.presentation.HomeUiEvent
import com.markusw.cosasdeunicorapp.ui.theme.home_bottom_bar_background
import com.markusw.cosasdeunicorapp.ui.theme.md_theme_light_primary

@Composable
fun BottomNavigationBar(
    state: HomeState,
    navController: NavHostController,
    modifier: Modifier = Modifier,
    onEvent: (HomeUiEvent) -> Unit
) {

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry?.destination
    val screens = listOf(
        HomeScreens.Chat,
        HomeScreens.News,
        HomeScreens.Home,
        HomeScreens.Documents,
        HomeScreens.More,
    )
    val selectedIndex = remember(currentDestination) {
        screens.indexOfFirst { it.route == currentDestination?.route }
    }

    AnimatedNavigationBar(
        selectedIndex = selectedIndex,
        ballColor = md_theme_light_primary,
        ballAnimation = Straight(tween(durationMillis = 300)),
        indentAnimation = Height(tween(durationMillis = 700)),
        barColor = home_bottom_bar_background,
        modifier = modifier
    ) {
        screens.forEach { screen ->
            BottomNavigationBarItem(
                label = screen.label,
                icon = screen.icon,
                selected = currentDestination?.route == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }
    }


}