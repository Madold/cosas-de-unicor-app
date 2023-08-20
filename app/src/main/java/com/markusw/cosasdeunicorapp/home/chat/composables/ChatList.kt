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
import com.markusw.cosasdeunicorapp.data.model.Message

@Composable
fun ChatList(
    items: List<Message>,
    modifier: Modifier = Modifier
) {

    val scrollState = rememberLazyListState()

    LaunchedEffect(key1 =  items) {
        scrollState.animateScrollToItem(items.lastIndex)
    }

    LazyColumn(
        modifier = modifier
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        state = scrollState
    ) {
        items(items) { message ->
            ChatItem(
                message = message,
                isFromCurrentUser = false
            )
        }
    }
}