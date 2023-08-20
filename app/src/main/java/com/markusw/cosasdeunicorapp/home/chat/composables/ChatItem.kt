package com.markusw.cosasdeunicorapp.home.chat.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.markusw.cosasdeunicorapp.data.model.Message

@Composable
fun ChatItem(
    message: Message,
    isFromCurrentUser: Boolean = false,
) {

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = if (isFromCurrentUser) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            if (!isFromCurrentUser) {
                Avatar(photoUrl = message.sender.photoUrl)
            }
            Column(
                horizontalAlignment = if (isFromCurrentUser) Alignment.End else Alignment.Start
            ) {
                ChatBubble(
                    content = message.content,
                    isFromCurrentUser = isFromCurrentUser,
                    cornerRadius = 20.dp
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    if (!isFromCurrentUser) {
                        NameLabel(name = message.sender.displayName)
                    }

                    Text(text = message.timestamp.toString())
                }
            }
        }
    }

}