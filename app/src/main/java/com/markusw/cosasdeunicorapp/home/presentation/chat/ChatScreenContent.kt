@file:OptIn(ExperimentalMaterial3Api::class, FlowPreview::class)

package com.markusw.cosasdeunicorapp.home.presentation.chat

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
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
    paddingValues: PaddingValues,
    mainNavController: NavController
) {

    val coroutineScope = rememberCoroutineScope()
    var isScrollToEndFABVisible by remember { mutableStateOf(false) }
    var isActionsMenuVisible by rememberSaveable { mutableStateOf(false) }
    val fabOffset by animateDpAsState(
        targetValue = if (isScrollToEndFABVisible) 0.dp else 1000.dp,
        label = ""
    )
    val usersCountText = remember(state.usersCount) {
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

    Box(
        modifier = Modifier.padding(bottom = paddingValues.calculateBottomPadding() + 8.dp)
    ) {
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
                                color = Color.White,
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Text(
                                text = usersCountText,
                                color = Color.White,
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
                                tint = Color.White,
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
                                            painter = painterResource(id = if (isGeneralChatNotificationsEnabled) R.drawable.ic_silenced_bell else R.drawable.ic_bell),
                                            contentDescription = null,
                                            tint = Color.White,
                                        )
                                        Text(
                                            notificationsText,
                                            color = Color.White
                                        )
                                    }
                                },
                                onClick = {
                                    isActionsMenuVisible = false
                                    onEvent(HomeUiEvent.ToggleGeneralChatNotifications)
                                }
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
                ChatList(
                    state = state,
                    scrollState = scrollState,
                    onReplyToMessage = { message ->
                        onEvent(HomeUiEvent.ReplyToMessage(message))
                    },
                    onRequestPreviousMessages = {
                        onEvent(HomeUiEvent.FetchPreviousGlobalMessages)
                    },
                    modifier = Modifier
                        .weight(1f)
                )
            }
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
        onEvent = {},
        paddingValues = PaddingValues(),
        mainNavController = rememberNavController()
    )
}