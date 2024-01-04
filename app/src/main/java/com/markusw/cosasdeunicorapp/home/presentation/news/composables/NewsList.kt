package com.markusw.cosasdeunicorapp.home.presentation.news.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.markusw.cosasdeunicorapp.core.presentation.PullRefreshIndicator
import com.markusw.cosasdeunicorapp.core.presentation.pullRefresh
import com.markusw.cosasdeunicorapp.core.presentation.rememberPullRefreshState
import com.markusw.cosasdeunicorapp.home.presentation.HomeState

@Composable
fun NewsList(
    state: HomeState,
    modifier: Modifier = Modifier,
    scrollState: LazyListState = rememberLazyListState(),
    onRequestPreviousNews: () -> Unit = {},
) {

    val pullRefreshState = rememberPullRefreshState(
        refreshing = false,
        onRefresh = {
            if (!state.isFetchingPreviousNews) {
                onRequestPreviousNews()
            }
        }
    )

    Box(
        modifier = modifier.pullRefresh(pullRefreshState)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            state = scrollState,
            reverseLayout = true
        ) {
            item {
                if (state.isFetchingPreviousNews) {
                    LinearProgressIndicator()
                }
            }
            items(state.newsList) { news ->
                NewsCard(
                    news = news,
                    onNewsClicked = {},
                )
            }
        }
        PullRefreshIndicator(
            modifier = Modifier.align(Alignment.TopCenter),
            refreshing = false,
            state = pullRefreshState
        )
    }


}