@file:OptIn(ExperimentalMaterial3Api::class, FlowPreview::class)

package com.markusw.cosasdeunicorapp.home.presentation.news

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.markusw.cosasdeunicorapp.R
import com.markusw.cosasdeunicorapp.core.ext.isScrolledToTheStart
import com.markusw.cosasdeunicorapp.core.presentation.AdmobBanner
import com.markusw.cosasdeunicorapp.home.presentation.HomeState
import com.markusw.cosasdeunicorapp.home.presentation.HomeUiEvent
import com.markusw.cosasdeunicorapp.home.presentation.chat.composables.RoundedIconButton
import com.markusw.cosasdeunicorapp.home.presentation.news.composables.NewsList
import com.markusw.cosasdeunicorapp.ui.theme.home_bottom_bar_background
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

@Composable
fun NewsScreenContent(
    state: HomeState,
    onEvent: (HomeUiEvent) -> Unit,
    scrollState: LazyListState = rememberLazyListState(),
) {

    val coroutineScope = rememberCoroutineScope()
    var isScrollToEndFABVisible by remember { mutableStateOf(false) }
    var isActionsMenuVisible by rememberSaveable { mutableStateOf(false) }
    val isNewsNotificationsEnabled = state.localSettings.isNewsNotificationsEnabled
    val notificationsText = remember(isNewsNotificationsEnabled) {
        if (isNewsNotificationsEnabled) {
            "Silenciar notificaciones"
        } else {
            "Activar notificaciones"
        }

    }
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
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Canal de Noticias",
                        color = MaterialTheme.colorScheme.surface,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = home_bottom_bar_background
                ),
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
                                        painter = painterResource(id = if(isNewsNotificationsEnabled) R.drawable.ic_silenced_bell else R.drawable.ic_bell),
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
                                onEvent(HomeUiEvent.ToggleNewsNotifications)
                            }
                        )
                    }
                }
            )
        },
        content = {
            NewsList(
                modifier = Modifier
                    .padding(it),
                state = state,
                scrollState = scrollState,
                onRequestPreviousNews = {
                    onEvent(HomeUiEvent.FetchPreviousNews)
                },
                onNewsLiked = { likedNews ->
                    onEvent(HomeUiEvent.LikeNews(likedNews))
                },
            )
        },
        floatingActionButton = {
            RoundedIconButton(
                icon = R.drawable.ic_arrow_down,
                modifier = Modifier.offset(y = fabOffset),
                onClick = {
                    coroutineScope.launch {
                        scrollState.animateScrollToItem(0)
                    }
                },
                enabled = isScrollToEndFABVisible
            )
        },
        bottomBar = {
            AdmobBanner()
        }
    )

}