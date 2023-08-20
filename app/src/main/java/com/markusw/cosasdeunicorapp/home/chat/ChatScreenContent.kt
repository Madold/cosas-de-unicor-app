@file:OptIn(ExperimentalMaterial3Api::class)

package com.markusw.cosasdeunicorapp.home.chat

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.markusw.cosasdeunicorapp.home.HomeState
import com.markusw.cosasdeunicorapp.home.chat.composables.ChatList

@Composable
fun ChatScreenContent(
    state: HomeState,
    onMessageChange: (String) -> Unit,
    onMessageSent: () -> Unit
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        bottomBar = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                TextField(
                    value = state.message,
                    onValueChange = onMessageChange,
                    trailingIcon = {
                        IconButton(onClick = onMessageSent) {
                            Icon(
                                imageVector = Icons.Default.Send,
                                contentDescription = "Send message"
                            )
                        }
                    }
                )
            }
        }
    ) {
        ChatList(
            items = state.globalChatList,
            modifier = Modifier.padding(it)
        )
    }
}