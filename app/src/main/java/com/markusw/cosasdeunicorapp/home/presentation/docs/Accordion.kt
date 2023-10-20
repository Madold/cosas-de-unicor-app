package com.markusw.cosasdeunicorapp.home.presentation.docs

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.markusw.cosasdeunicorapp.R


@Composable
fun Accordion(
    title: String,
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {

    var isExpanded by rememberSaveable {
        mutableStateOf(false)
    }
    val arrowRotation by animateFloatAsState(
        targetValue = if (isExpanded) -180f else 0f,
        label = "Arrow rotation"
    )
    val accordionBackgroundColor by animateColorAsState(
        targetValue = if (isExpanded) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
        label = ""
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(8.dp))
                .background(color = accordionBackgroundColor)
                .clickable {
                    isExpanded = !isExpanded
                }
                .padding(horizontal = 8.dp, vertical = 16.dp)
            ,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = title)
            Icon(
                painter = painterResource(id = R.drawable.ic_slim_arrow_down),
                contentDescription = null,
                modifier = Modifier.rotate(arrowRotation)
            )
        }
        AnimatedVisibility(visible = isExpanded) {
            Column {
                content()
            }
        }
    }

}