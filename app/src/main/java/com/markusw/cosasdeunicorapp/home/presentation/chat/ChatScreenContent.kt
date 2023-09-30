@file:OptIn(ExperimentalMaterial3Api::class, FlowPreview::class)

package com.markusw.cosasdeunicorapp.home.presentation.chat

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.markusw.cosasdeunicorapp.R
import com.markusw.cosasdeunicorapp.core.ext.isScrolledToTheStart
import com.markusw.cosasdeunicorapp.home.presentation.HomeState
import com.markusw.cosasdeunicorapp.home.presentation.HomeUiEvent
import com.markusw.cosasdeunicorapp.home.presentation.chat.composables.ChatList
import com.markusw.cosasdeunicorapp.home.presentation.chat.composables.MessageField
import com.markusw.cosasdeunicorapp.home.presentation.chat.composables.RoundedIconButton
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

@Composable
fun ChatScreenContent(
    state: HomeState,
    onEvent: (HomeUiEvent) -> Unit
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
            isScrollToEndFABVisible = !scrollState.isScrolledToTheStart()
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(title = {
                Text(text = stringResource(id = R.string.global_chat))
            })
        },
        bottomBar = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                MessageField(
                    modifier = Modifier.fillMaxWidth(0.9f),
                    value = state.message,
                    onValueChange = {
                        onEvent(HomeUiEvent.MessageChanged(it))
                    },
                    isSendIconEnabled = state.message.isNotBlank(),
                    onSendIconClick = {
                        onEvent(HomeUiEvent.SendMessageToGlobalChat)
                        coroutineScope.launch {
                            scrollState.animateScrollToItem(0)
                        }
                    },
                    messageToReply = state.repliedMessage,
                    onDismissReply = {
                        // Hola bb
                        onEvent(HomeUiEvent.ClearReplyMessage)
                    }
                )
            }
        },
        floatingActionButton = {
            RoundedIconButton(
                icon = R.drawable.ic_arrow_down,
                onClick = {
                    coroutineScope.launch {
                        scrollState.animateScrollToItem(0)
                    }
                },
                modifier = Modifier.offset(y = fabOffset),
            )
        }
    ) {
        ChatList(
            state = state,
            modifier = Modifier.padding(it),
            scrollState = scrollState,
            onReplyToMessage = { message ->
                onEvent(HomeUiEvent.ReplyToMessage(message))
            },
            onRequestPreviousMessages = {
                onEvent(HomeUiEvent.FetchPreviousGlobalMessages)
            }
        )
    }
}

