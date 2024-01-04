@file:OptIn(ExperimentalMaterial3Api::class)

package com.markusw.cosasdeunicorapp.home.presentation.news

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.markusw.cosasdeunicorapp.home.presentation.HomeState
import com.markusw.cosasdeunicorapp.home.presentation.HomeUiEvent
import com.markusw.cosasdeunicorapp.home.presentation.news.composables.NewsList
import timber.log.Timber

@Composable
fun NewsScreenContent(
    state: HomeState,
    onEvent: (HomeUiEvent) -> Unit,
    scrollState: LazyListState = rememberLazyListState(),
) {

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Noticias")
                }
            )
        },
        content = {
            NewsList(
                modifier = Modifier
                    .padding(it),
                state = state,
                scrollState = scrollState,
                onRequestPreviousNews = {
                    onEvent(HomeUiEvent.FetchPreviousNews)
                }
            )
        }
    )

}