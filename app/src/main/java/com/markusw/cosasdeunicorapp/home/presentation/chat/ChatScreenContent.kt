@file:OptIn(ExperimentalMaterial3Api::class, FlowPreview::class)

package com.markusw.cosasdeunicorapp.home.presentation.chat

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.markusw.cosasdeunicorapp.R
import com.markusw.cosasdeunicorapp.core.ext.isScrolledToTheStart
import com.markusw.cosasdeunicorapp.home.presentation.HomeState
import com.markusw.cosasdeunicorapp.home.presentation.HomeUiEvent
import com.markusw.cosasdeunicorapp.home.presentation.chat.composables.ChatList
import com.markusw.cosasdeunicorapp.home.presentation.chat.composables.MessageField
import com.markusw.cosasdeunicorapp.home.presentation.chat.composables.RoundedIconButton
import com.markusw.cosasdeunicorapp.ui.theme.home_bottom_bar_background
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

@Composable
fun ChatScreenContent(
    state: HomeState,
    onEvent: (HomeUiEvent) -> Unit,
    scrollState: LazyListState = rememberLazyListState(),
) {

    val coroutineScope = rememberCoroutineScope()
    var isScrollToEndFABVisible by remember { mutableStateOf(false) }
    var isActionsMenuVisible by rememberSaveable { mutableStateOf(false) }
    val fabOffset by animateDpAsState(
        targetValue = if (isScrollToEndFABVisible) 0.dp else 1000.dp,
        label = ""
    )
    val usersCountText = remember {
        "${state.usersCount} Usuarios"
    }
    val isGeneralChatNotificationsEnabled = state.localSettings.isGeneralChatNotificationsEnabled
    val notificationsText = remember(isGeneralChatNotificationsEnabled) {
        if (isGeneralChatNotificationsEnabled) {
            "Silenciar notificaciones"
        } else {
            "Activar notificaciones"
        }

    }

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
            CenterAlignedTopAppBar(
                title = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(id = R.string.global_chat),
                            color = MaterialTheme.colorScheme.surface,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            text = usersCountText,
                            color = MaterialTheme.colorScheme.surface,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = home_bottom_bar_background
                ),
                navigationIcon = {
                    Image(
                        painter = painterResource(id = R.drawable.app_icon),
                        contentDescription = null,
                        modifier = Modifier
                            .size(62.dp)
                    )
                },
                actions = {
                    IconButton(onClick = { isActionsMenuVisible = true }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.surface,
                        )
                    }

                    DropdownMenu(
                        expanded = isActionsMenuVisible,
                        onDismissRequest = { isActionsMenuVisible = false },
                        modifier = Modifier.background(home_bottom_bar_background)
                    ) {
                        DropdownMenuItem(
                            text = {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        painter = painterResource(id = if(isGeneralChatNotificationsEnabled) R.drawable.ic_silenced_bell else R.drawable.ic_bell),
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.surface,
                                    )
                                    Text(
                                        notificationsText,
                                        color = MaterialTheme.colorScheme.surface
                                    )
                                }
                            },
                            onClick = {
                                isActionsMenuVisible = false
                                onEvent(HomeUiEvent.ToggleGeneralChatNotifications)
                            },
                            colors = MenuDefaults.itemColors(

                            )
                        )
                    }
                }
            )
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
                        onEvent(HomeUiEvent.ClearReplyMessage)
                    },
                    messageToReply = state.repliedMessage,
                    onDismissReply = {
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
        Column(
            modifier = Modifier.padding(it)
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            ChatList(
                state = state,
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
}

@Preview(
    showBackground = true
)
@Composable
fun ChatScreenPreview() {
    ChatScreenContent(
        state = HomeState(
            usersCount = 23
        ),
        onEvent = {}
    )
}