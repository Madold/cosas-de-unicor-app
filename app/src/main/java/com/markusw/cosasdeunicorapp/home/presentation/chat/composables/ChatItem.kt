@file:OptIn(ExperimentalMaterial3Api::class)

package com.markusw.cosasdeunicorapp.home.presentation.chat.composables

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.markusw.cosasdeunicorapp.R
import com.markusw.cosasdeunicorapp.core.utils.TextUtils
import com.markusw.cosasdeunicorapp.home.domain.model.Message
import com.markusw.cosasdeunicorapp.home.domain.model.MessageContent
import com.markusw.cosasdeunicorapp.core.domain.model.User
import com.markusw.cosasdeunicorapp.core.presentation.ProfileAvatar

@Composable
fun ChatItem(
    message: Message,
    isFromCurrentUser: Boolean = false,
    onReplyToMessage: (Message) -> Unit = {},
    swipeEnabled: Boolean = true
) {

    val formattedTimestamp = remember(key1 = message.timestamp) {
        TextUtils.formatTimestamp(message.timestamp)
    }
    val horizontalArrangement = remember(key1 = isFromCurrentUser) {
        if (isFromCurrentUser) Arrangement.End else Arrangement.Start
    }
    val horizontalAlignment = remember(key1 = isFromCurrentUser) {
        if (isFromCurrentUser) Alignment.End else Alignment.Start
    }
    val dismissState = rememberDismissState(
        confirmValueChange = { dismissValue ->
            if (dismissValue == DismissValue.DismissedToEnd) {
                onReplyToMessage(message)
            }
            false
        }
    )
    val replyIconOffset by animateDpAsState(
        targetValue = if (dismissState.dismissDirection == DismissDirection.StartToEnd) 0.dp
        else -(1000).dp,
        label = ""
    )

    SwipeToDismiss(
        state = dismissState,
        background = {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    modifier = Modifier.offset(x = replyIconOffset),
                    painter = painterResource(id = R.drawable.ic_reply),
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(4.dp))
            }
        },
        directions = if(swipeEnabled) setOf(DismissDirection.StartToEnd) else emptySet() ,
        dismissContent = {
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
                        ProfileAvatar(
                            imageUrl = message.sender.photoUrl,
                            size = 32.dp
                        )
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
    )

}

@Preview(showBackground = true)
@Composable
fun ChatItemPreview() {
    ChatItem(
        message = Message(
            MessageContent(text = "Example message"),
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
