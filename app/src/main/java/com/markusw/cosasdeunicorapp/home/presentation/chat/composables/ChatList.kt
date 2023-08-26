package com.markusw.cosasdeunicorapp.home.presentation.chat.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markusw.cosasdeunicorapp.core.ext.isSecondLastItemVisible
import com.markusw.cosasdeunicorapp.home.presentation.HomeState

@Composable
fun ChatList(
    state: HomeState,
    modifier: Modifier = Modifier,
    scrollState: LazyListState = rememberLazyListState()
) {

    val globalChatList = state.globalChatList

    LaunchedEffect(key1 = Unit) {
        scrollState.animateScrollToItem(globalChatList.size)
    }

    LaunchedEffect(key1 = globalChatList) {
        if (scrollState.isSecondLastItemVisible()) {
            scrollState.animateScrollToItem(globalChatList.size)
        }
    }

    LazyColumn(
        modifier = modifier
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        state = scrollState
    ) {
        items(globalChatList) { message ->
            ChatItem(
                message = message,
                isFromCurrentUser = state.currentUser.displayName == message.sender.displayName
            )
        }
    }
}
