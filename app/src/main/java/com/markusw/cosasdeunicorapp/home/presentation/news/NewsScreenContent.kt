@file:OptIn(ExperimentalMaterial3Api::class, FlowPreview::class)

package com.markusw.cosasdeunicorapp.home.presentation.news

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markusw.cosasdeunicorapp.R
import com.markusw.cosasdeunicorapp.core.ext.isScrolledToTheEnd
import com.markusw.cosasdeunicorapp.core.ext.isScrolledToTheStart
import com.markusw.cosasdeunicorapp.home.presentation.HomeState
import com.markusw.cosasdeunicorapp.home.presentation.HomeUiEvent
import com.markusw.cosasdeunicorapp.home.presentation.chat.composables.RoundedIconButton
import com.markusw.cosasdeunicorapp.home.presentation.news.composables.NewsList
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
fun NewsScreenContent(
    state: HomeState,
    onEvent: (HomeUiEvent) -> Unit,
    scrollState: LazyListState = rememberLazyListState(),
) {

    val coroutineScope = rememberCoroutineScope()
    var isScrollToEndFABVisible by remember { mutableStateOf(false) }
    val fabOffset by animateDpAsState(
        targetValue = if (isScrollToEndFABVisible) 0.dp else 1000.dp,
        label = ""
    )

    LaunchedEffect(key1 = scrollState) {
        snapshotFlow {
            scrollState.firstVisibleItemIndex
        }.debounce(500).collectLatest {
            isScrollToEndFABVisible = !scrollState.isScrolledToTheStart()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Canal de Noticias")
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
        },
        floatingActionButton = {
            RoundedIconButton(
                icon = R.drawable.ic_arrow_down,
                modifier = Modifier.offset(y = fabOffset),
                onClick = {
                    coroutineScope.launch {
                        scrollState.animateScrollToItem(0)
                    }
                },
                enabled = isScrollToEndFABVisible
            )
        }
    )

}