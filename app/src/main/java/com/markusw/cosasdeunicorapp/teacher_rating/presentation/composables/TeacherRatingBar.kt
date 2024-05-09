package com.markusw.cosasdeunicorapp.teacher_rating.presentation.composables

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.markusw.cosasdeunicorapp.core.utils.TextUtils

const val RATING_BAR_LABEL_WEIGHT = 0.3f
@Composable
fun TeacherRatingBar(
    label: @Composable () -> Unit,
    percentage: Float,
    count: Int,
    modifier: Modifier = Modifier,
    barHeight: Dp = 28.dp,
    barColor: Color = Color.Gray,
    isInsideCard: Boolean = false
) {

    var animationPlayed by remember {
        mutableStateOf(false)
    }
    val animatedPercentage by animateFloatAsState(
        targetValue = if (animationPlayed) percentage else 0f,
        label = "Bar animation",
        animationSpec = tween(
            durationMillis = 500
        )
    )
    val isSystemInDarkTheme = isSystemInDarkTheme()

    LaunchedEffect(key1 = Unit) {
        animationPlayed = true
    }

    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        label()

        Row(modifier = Modifier
            .weight(1f)
            .height(barHeight),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth(animatedPercentage)
                    .clip(RoundedCornerShape(16.dp))
                    .background(barColor)
                    .height(28.dp)
                ,
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = TextUtils.formatPercentage(percentage),
                    color = Color.White
                )
            }
            Text(text = "(${count})", color = if (isSystemInDarkTheme && !isInsideCard) Color.White else MaterialTheme.colorScheme.onSurface)
        }

    }

}