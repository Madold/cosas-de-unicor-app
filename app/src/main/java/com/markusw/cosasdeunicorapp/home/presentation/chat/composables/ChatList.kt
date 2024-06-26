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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markusw.cosasdeunicorapp.core.presentation.PullRefreshIndicator
import com.markusw.cosasdeunicorapp.core.presentation.pullRefresh
import com.markusw.cosasdeunicorapp.core.presentation.rememberPullRefreshState
import com.markusw.cosasdeunicorapp.home.domain.model.Message
import com.markusw.cosasdeunicorapp.home.presentation.HomeState

@Composable
fun ChatList(
    state: HomeState,
    modifier: Modifier = Modifier,
    scrollState: LazyListState = rememberLazyListState(),
    onRequestPreviousMessages: () -> Unit = {},
    onReplyToMessage: (Message) -> Unit = {}
) {

    val globalChatList = state.globalChatList
    val pullRefreshState = rememberPullRefreshState(
        refreshing = false,
        onRefresh = {
            if (!state.isFetchingPreviousGlobalMessages) {
                onRequestPreviousMessages()
            }
        }
    )

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
            reverseLayout = true,
        ) {
            items(globalChatList, key = { message -> message.timestamp }) { message ->
                ChatItem(
                    message = message,
                    isFromCurrentUser = state.currentUser.uid == message.sender.uid,
                    onReplyToMessage = onReplyToMessage
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
