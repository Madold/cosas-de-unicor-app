@file:OptIn(ExperimentalMaterial3Api::class)

package com.markusw.cosasdeunicorapp.home

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.markusw.cosasdeunicorapp.home.chat.ChatScreenContent
import com.markusw.cosasdeunicorapp.home.composables.BottomNavigationBar
import com.markusw.cosasdeunicorapp.home.docs.DocsScreenContent
import com.markusw.cosasdeunicorapp.home.main.HomeScreenContent
import com.markusw.cosasdeunicorapp.home.news.NewsScreenContent

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
            HomeScreenContent()
        }
        composable(route = HomeScreens.Chat.route) {
            ChatScreenContent(
                state = uiState,
                onMessageChange = viewModel::onMessageChange,
                onMessageSent = viewModel::onMessageSent
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

