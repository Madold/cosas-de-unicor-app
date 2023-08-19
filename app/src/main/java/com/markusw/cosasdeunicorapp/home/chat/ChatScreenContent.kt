@file:OptIn(ExperimentalMaterial3Api::class)

package com.markusw.cosasdeunicorapp.home.chat

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.markusw.cosasdeunicorapp.data.model.Message

@Composable
fun ChatScreenContent(
    globalChatList: List<Message>,
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
    ) {
        LazyColumn(modifier = Modifier.padding(it)) {
            items(globalChatList) { message ->
                Text(text = message.content)
            }
        }
    }
}