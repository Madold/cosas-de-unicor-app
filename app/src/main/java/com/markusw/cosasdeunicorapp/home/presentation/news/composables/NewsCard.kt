package com.markusw.cosasdeunicorapp.home.presentation.news.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.markusw.cosasdeunicorapp.core.presentation.ExpandableText
import com.markusw.cosasdeunicorapp.home.domain.model.News

@Composable
fun NewsCard(
    modifier: Modifier = Modifier,
    news: News,
    onNewsClicked: (News) -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth(0.9f),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNewsClicked(news) }
                    .padding(8.dp)
            ) {
                SubcomposeAsyncImage(
                    model = news.coverUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.FillWidth
                )
                Text(
                    text = news.title,
                    style = MaterialTheme.typography.titleLarge
                )
                Divider()
                /*Text(
                    text = news.content,
                    maxLines = 10,
                    style = MaterialTheme.typography.bodyLarge,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Justify
                )*/
                ExpandableText(
                    text = news.content,
                    collapsedMaxLine = 5,
                    showMoreText = "...Mostrar más",
                    showLessText = " Mostrar menos",
                    textAlign = TextAlign.Justify,
                )
            }
        }
    }
}

@Composable
fun NewsCardPreview() {

}