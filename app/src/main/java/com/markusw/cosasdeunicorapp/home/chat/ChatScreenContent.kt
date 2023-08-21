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
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.markusw.cosasdeunicorapp.home.HomeState
import com.markusw.cosasdeunicorapp.home.chat.composables.ChatList
import com.markusw.cosasdeunicorapp.home.chat.composables.MessageField

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
                MessageField(
                    modifier = Modifier.fillMaxWidth(0.9f),
                    value = state.message,
                    onValueChange = onMessageChange,
                    isSendIconEnabled = state.message.isNotEmpty(),
                    onSendIconClick = onMessageSent
                )
            }
        }
    ) {
        ChatList(
            state = state,
            modifier = Modifier.padding(it)
        )
    }
}