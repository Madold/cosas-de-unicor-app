package com.markusw.cosasdeunicorapp.core.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.markusw.cosasdeunicorapp.R
import com.markusw.cosasdeunicorapp.core.ext.shimmerEffect

@Composable
fun ProfileAvatar(
    imageUrl: String,
    modifier: Modifier = Modifier,
    size: Dp = 24.dp,
) {

    SubcomposeAsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .crossfade(true)
            .error(R.drawable.default_user)
            .build(),
        contentDescription = null,
        modifier = modifier
            .clip(CircleShape)
            .size(size),
        loading = {
            Box(
                modifier = modifier
                    .clip(CircleShape)
                    .size(size)
                    .shimmerEffect()
            )
        }
    )

}