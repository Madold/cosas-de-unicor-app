@file:OptIn(ExperimentalMaterial3Api::class, FlowPreview::class)

package com.markusw.cosasdeunicorapp.home.presentation.chat

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.markusw.cosasdeunicorapp.R
import com.markusw.cosasdeunicorapp.core.ext.isScrolledToTheEnd
import com.markusw.cosasdeunicorapp.home.presentation.HomeState
import com.markusw.cosasdeunicorapp.home.presentation.chat.composables.ChatList
import com.markusw.cosasdeunicorapp.home.presentation.chat.composables.MessageField
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

@Composable
fun ChatScreenContent(
    state: HomeState,
    onMessageChange: (String) -> Unit,
    onMessageSent: () -> Unit
) {

    val scrollState = rememberLazyListState()
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
            isScrollToEndFABVisible = !scrollState.isScrolledToTheEnd()
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        bottomBar = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                MessageField(
                    modifier = Modifier.fillMaxWidth(0.9f),
                    value = state.message,
                    onValueChange = onMessageChange,
                    isSendIconEnabled = state.message.isNotEmpty(),
                    onSendIconClick = {
                        onMessageSent()
                        coroutineScope.launch {
                            scrollState.animateScrollToItem(state.globalChatList.size)
                        }
                    }
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    coroutineScope.launch {
                        scrollState.animateScrollToItem(state.globalChatList.size)
                    }
                },
                content = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_down),
                        contentDescription = "Scroll to bottom"
                    )
                },
                modifier = Modifier.offset(y = fabOffset),
                shape = CircleShape
            )
        }
    ) {
        ChatList(
            state = state,
            modifier = Modifier.padding(it),
            scrollState = scrollState
        )
    }
}
