package com.markusw.cosasdeunicorapp.home.presentation.docs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun AccordionItem(
    title: String,
    onItemClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    Text(text = title, modifier = modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(8.dp))
        .clickable(onClick = onItemClick)
        .padding(vertical = 16.dp, horizontal = 16.dp)

    )


}