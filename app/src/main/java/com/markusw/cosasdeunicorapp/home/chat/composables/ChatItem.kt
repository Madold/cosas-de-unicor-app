package com.markusw.cosasdeunicorapp.home.chat.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.markusw.cosasdeunicorapp.core.utils.TextUtils
import com.markusw.cosasdeunicorapp.data.model.Message
import com.markusw.cosasdeunicorapp.data.model.User

@Composable
fun ChatItem(
    message: Message,
    isFromCurrentUser: Boolean = false,
) {

    val formattedTimestamp = remember(key1 = message.timestamp) { TextUtils.formatTimestamp(message.timestamp) }
    val horizontalArrangement = remember(key1 = isFromCurrentUser) { if (isFromCurrentUser) Arrangement.End else Arrangement.Start }
    val horizontalAlignment = remember(key1 = isFromCurrentUser) { if (isFromCurrentUser) Alignment.End else Alignment.Start }

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            if (!isFromCurrentUser) {
                Avatar(photoUrl = message.sender.photoUrl)
            }
            Column(
                horizontalAlignment = horizontalAlignment
            ) {
                ChatBubble(
                    content = message.content,
                    isFromCurrentUser = isFromCurrentUser,
                    cornerRadius = 20.dp
                )
                Row(
                    horizontalArrangement = Arrangement.Start,
                ) {
                    if (!isFromCurrentUser) {
                        NameLabel(name = message.sender.displayName)
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = formattedTimestamp,
                        color = Color.Gray
                    )
                }
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun ChatItemPreview() {
    ChatItem(
        message = Message(
            "Hola",
            User(
                "Markus",
                "mmmm",
                "https://avatars.githubusercontent.com/u/18093076?v=4",
                "assas"
            ),
            1627777777777L
        ), isFromCurrentUser = true
    )
}
