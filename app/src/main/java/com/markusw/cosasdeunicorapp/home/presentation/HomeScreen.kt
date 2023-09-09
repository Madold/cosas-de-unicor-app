package com.markusw.cosasdeunicorapp.home.presentation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.markusw.cosasdeunicorapp.home.presentation.chat.ChatScreenContent
import com.markusw.cosasdeunicorapp.home.presentation.composables.BottomNavigationBar
import com.markusw.cosasdeunicorapp.home.presentation.docs.DocsScreenContent
import com.markusw.cosasdeunicorapp.home.presentation.main.HomeScreenContent
import com.markusw.cosasdeunicorapp.home.presentation.news.NewsScreenContent

@Composable
fun HomeScreen() {

    val navController = rememberNavController()
    val viewModel: HomeViewModel = hiltViewModel()

    Scaffold(
        content = {
            HomeHost(
                navController = navController,
                contentPadding = it,
                viewModel = viewModel
            )
        },
        bottomBar = {
            BottomNavigationBar(
                navController = navController
            )
        }
    )
}

@Composable
private fun HomeHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    contentPadding: PaddingValues,
    viewModel: HomeViewModel
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    NavHost(
        modifier = modifier
            .fillMaxSize()
            .padding(contentPadding),
        navController = navController,
        startDestination = HomeScreens.Home.route
    ) {
        composable(route = HomeScreens.Home.route) {
            HomeScreenContent(
                onEvent = viewModel::onEvent
            )
        }
        composable(route = HomeScreens.Chat.route) {
            ChatScreenContent(
                state = uiState,
                onEvent = viewModel::onEvent
            )
        }
        composable(route = HomeScreens.News.route) {
            NewsScreenContent()
        }
        composable(route = HomeScreens.Documents.route) {
            DocsScreenContent()
        }
        composable(route = HomeScreens.More.route) {
            Text(text = "Coming soon...")
        }
    }
}

