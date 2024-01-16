@file:OptIn(ExperimentalMaterial3Api::class)

package com.markusw.cosasdeunicorapp.home.presentation.news.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.markusw.cosasdeunicorapp.R
import com.markusw.cosasdeunicorapp.core.domain.model.User
import com.markusw.cosasdeunicorapp.core.ext.invert
import com.markusw.cosasdeunicorapp.core.presentation.utils.calculateDominantColor
import com.markusw.cosasdeunicorapp.core.utils.TextUtils
import com.markusw.cosasdeunicorapp.home.domain.model.News

@Composable
fun NewsCard(
    modifier: Modifier = Modifier,
    news: News,
    onNewsLiked: (News) -> Unit = {},
    loggedUser: User = User(),
) {

    var isNewsDetailsDialogVisible by rememberSaveable { mutableStateOf(false) }
    val isLiked = news.likedBy.contains(loggedUser)
    val heartIcon = if (isLiked) R.drawable.ic_heart else R.drawable.ic_heart_outline
    val formattedTimestamp = remember {
        TextUtils.formatDate(news.timestamp)
    }

    Box(
        modifier = modifier
            .clickable { isNewsDetailsDialogVisible = true }
            .padding(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 0.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
        ) {
            SubcomposeAsyncImage(
                model = news.coverUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp)),
                contentScale = ContentScale.FillWidth
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = news.title,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_clock),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                    Text(text = formattedTimestamp)
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AnimatedLikeHeart(
                        isLiked = isLiked,
                        onToggleLike = { onNewsLiked(news) }
                    )
                    Text(text = news.likedBy.size.toString())
                }
            }
        }
    }

    Divider()

    if (isNewsDetailsDialogVisible) {
        NewsDetailsDialog(
            news = news,
            onDismiss = { isNewsDetailsDialogVisible = false }
        )
    }

}

@Composable
private fun NewsDetailsDialog(
    news: News,
    onDismiss: () -> Unit = {},
) {

    val defaultDominantColor = MaterialTheme.colorScheme.surface
    val defaultDominantColorValue = defaultDominantColor.toArgb()
    var dominantColor by remember {
        mutableStateOf(defaultDominantColor)
    }
    val dominantColorInverted = dominantColor.invert()


    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Card(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()

                ) {
                    Box(
                        modifier = Modifier
                            .background(
                            brush = Brush.verticalGradient(
                                0.0f to Color.Transparent,
                                500.0f to MaterialTheme.colorScheme.surface,
                                startY = 0.0f,
                                endY = Float.POSITIVE_INFINITY
                            )
                        )
                    )
                    SubcomposeAsyncImage(
                        model = ImageRequest
                            .Builder(LocalContext.current)
                            .data(news.coverUrl)
                            .crossfade(true)
                            .build()
                        ,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .drawWithCache {
                                onDrawWithContent {
                                    drawContent()
                                    drawRect(
                                        Brush.verticalGradient(
                                            0.2f to Color.Transparent,
                                            1F to dominantColor
                                        )
                                    )
                                }
                            }
                        ,
                        contentScale = ContentScale.FillWidth,
                        onSuccess = { painterResult ->
                            val drawable = painterResult.result.drawable
                            val dominantColorFromImage = calculateDominantColor(drawable, defaultDominantColorValue)
                            dominantColor = dominantColorFromImage
                        }
                    )
                    Text(
                        text = news.title,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier
                            .padding(8.dp)
                            .align(alignment = Alignment.BottomStart),
                        color = dominantColorInverted
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = news.content,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Justify
                    )
                }
            }
        }
    }


}
