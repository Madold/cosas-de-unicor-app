package com.markusw.cosasdeunicorapp.home.presentation.chat.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markusw.cosasdeunicorapp.core.ext.isSecondLastItemVisible
import com.markusw.cosasdeunicorapp.core.presentation.PullRefreshIndicator
import com.markusw.cosasdeunicorapp.core.presentation.pullRefresh
import com.markusw.cosasdeunicorapp.core.presentation.rememberPullRefreshState
import com.markusw.cosasdeunicorapp.home.presentation.HomeState
import com.markusw.cosasdeunicorapp.home.presentation.HomeUiEvent

@Composable
fun ChatList(
    state: HomeState,
    modifier: Modifier = Modifier,
    scrollState: LazyListState = rememberLazyListState(),
    onEvent: (HomeUiEvent) -> Unit
) {

    val globalChatList = state.globalChatList
    val pullRefreshState = rememberPullRefreshState(
        refreshing = false,
        onRefresh = {
            onEvent(HomeUiEvent.FetchPreviousGlobalMessages)
        }
    )

    LaunchedEffect(key1 = globalChatList) {
        if (scrollState.isSecondLastItemVisible()) {
            scrollState.animateScrollToItem(globalChatList.size)
        }
    }

    Box(
        modifier = modifier
            .pullRefresh(pullRefreshState)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            state = scrollState,
            reverseLayout = true
        ) {
            items(globalChatList) { message ->
                ChatItem(
                    message = message,
                    isFromCurrentUser = state.currentUser.displayName == message.sender.displayName
                )
            }
        }
        PullRefreshIndicator(
            modifier = Modifier.align(Alignment.TopCenter),
            refreshing = false,
            state = pullRefreshState
        )
        if (state.isFetchingPreviousGlobalMessages) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }
    }
}
