package com.markusw.cosasdeunicorapp.teacher_rating.presentation.composables

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun RatingChip(
    onClick: () -> Unit,
    label: @Composable () -> Unit,
    backgroundColor: Color,
    modifier: Modifier = Modifier,
    selected: Boolean = false
) {

    AssistChip(
        onClick = onClick,
        label = label,
        colors = AssistChipDefaults.assistChipColors(
            containerColor = if(selected) backgroundColor else Color.Gray,
        ),
        shape = CircleShape,
        modifier = modifier
    )

}