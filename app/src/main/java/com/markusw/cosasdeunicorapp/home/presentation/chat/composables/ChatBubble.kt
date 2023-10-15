package com.markusw.cosasdeunicorapp.home.presentation.chat.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.markusw.cosasdeunicorapp.home.domain.model.MessageContent

@Composable
fun ChatBubble(
    content: MessageContent,
    modifier: Modifier = Modifier,
    isFromCurrentUser: Boolean = false,
    cornerRadius: Dp = 20.dp
) {

    val chatBubbleShape = remember(isFromCurrentUser) {
        if (isFromCurrentUser) RoundedCornerShape(
            topStart = cornerRadius,
            topEnd = cornerRadius,
            bottomStart = cornerRadius,
            bottomEnd = 0.dp
        ) else RoundedCornerShape(
            topStart = cornerRadius,
            topEnd = cornerRadius,
            bottomStart = 0.dp,
            bottomEnd = cornerRadius
        )
    }

    val backgroundColor = if (isFromCurrentUser) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.tertiaryContainer

    Column(
        modifier = modifier
            .clip(chatBubbleShape)
            .background(backgroundColor)
            .padding(all = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
        content.imageUrl?.let { imageUrl ->
            AsyncImage(
                model = imageUrl,
                contentDescription = "Image message"
            )
        }
        content.replyTo?.let { repliedMessage ->
            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(color = MaterialTheme.colorScheme.surface)
                    .padding(all = 8.dp)
                ,
            ) {
                Text(text = repliedMessage.sender.displayName, style = TextStyle(
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.Bold
                ))
                Text(text = repliedMessage.content.text)
            }
        }
        Text(text = content.text)
    }
}