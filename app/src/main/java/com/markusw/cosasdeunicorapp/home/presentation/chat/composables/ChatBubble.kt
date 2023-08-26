package com.markusw.cosasdeunicorapp.home.presentation.chat.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ChatBubble(
    content: String,
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

    Box(
        modifier = modifier
            .clip(chatBubbleShape)
            .background(backgroundColor)
            .padding(all = 8.dp),

        ) {
        Text(text = content)
    }
}