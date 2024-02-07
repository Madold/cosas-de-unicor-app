package com.markusw.cosasdeunicorapp.home.presentation.news.composables

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.markusw.cosasdeunicorapp.R
import com.markusw.cosasdeunicorapp.ui.theme.heart_color


@Composable
fun AnimatedLikeHeart(
    isLiked: Boolean,
    onToggleLike: () -> Unit,
    modifier: Modifier = Modifier
) {

    AnimatedContent(
        targetState = isLiked,
        modifier = modifier,
        label = "Animated Heart",
        transitionSpec = {
            scaleIn(
                initialScale = if (isLiked) 0f else 1f,
                animationSpec = tween(durationMillis = 500)
            ) togetherWith scaleOut(
                targetScale = if (isLiked) 1f else 0f,
                animationSpec = tween(durationMillis = 500)
            )

        }
    ) {
        if (it) {
            IconButton(onClick = onToggleLike) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_heart),
                    contentDescription = null,
                    tint = heart_color
                )
            }
        } else {
            IconButton(onClick = onToggleLike) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_heart_outline),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }
        }

    }

}