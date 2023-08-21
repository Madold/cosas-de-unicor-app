package com.markusw.cosasdeunicorapp.home.chat.composables

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.markusw.cosasdeunicorapp.R

@Composable
fun Avatar(
    photoUrl: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    AsyncImage(
        model = ImageRequest.Builder(context)
            .data(photoUrl)
            .error(R.drawable.default_user)
            .build(),
        contentDescription = "Profile picture",
        modifier = modifier
            .size(32.dp)
            .clip(CircleShape)
    )
}