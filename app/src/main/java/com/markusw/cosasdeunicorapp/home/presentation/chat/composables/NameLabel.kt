package com.markusw.cosasdeunicorapp.home.presentation.chat.composables

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight

@Composable
fun NameLabel(
    name: String
) {
    Text(
        text = name,
        fontWeight = FontWeight.Bold
    )
}