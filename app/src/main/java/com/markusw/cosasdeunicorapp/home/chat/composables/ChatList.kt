package com.markusw.cosasdeunicorapp.home.chat.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markusw.cosasdeunicorapp.home.HomeState

@Composable
fun ChatList(
    state: HomeState,
    modifier: Modifier = Modifier
) {

    val scrollState = rememberLazyListState()
    val globalChatList = state.globalChatList

    LaunchedEffect(key1 = globalChatList) {
        scrollState.animateScrollToItem(globalChatList.size)
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
