package com.markusw.cosasdeunicorapp.home.chat.composables

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun Avatar(
    photoUrl: String,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        model = photoUrl,
        contentDescription = "Profile picture",
        modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
    )
}