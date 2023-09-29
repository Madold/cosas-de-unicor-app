@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.markusw.cosasdeunicorapp.home.presentation.chat.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.markusw.cosasdeunicorapp.R
import com.markusw.cosasdeunicorapp.home.domain.model.Message
import com.markusw.cosasdeunicorapp.ui.theme.CosasDeUnicorAppTheme
import com.markusw.cosasdeunicorapp.ui.theme.md_theme_light_primary
import com.markusw.cosasdeunicorapp.ui.theme.message_field_color

@Composable
fun MessageField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    onSendIconClick: () -> Unit = {},
    isEnabled: Boolean = true,
    isSendIconEnabled: Boolean = true,
    messageToReply: Message? = null,
    onDismissReply: () -> Unit = {}
) {

    val sendIconColor by animateColorAsState(
        targetValue = if (isSendIconEnabled) md_theme_light_primary else md_theme_light_primary.copy(alpha = 0.5f),
        label = ""
    )

    Column(
        modifier = modifier
    ) {
        AnimatedVisibility(visible = messageToReply != null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(message_field_color)
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ){
                    Text(text = "Reply to: ${messageToReply?.sender?.displayName}")
                    Text(text = messageToReply?.content ?: "")
                }
                IconButton(onClick = onDismissReply) {
                    Icon(imageVector = Icons.Default.Clear, contentDescription = null)
                }
            }
        }
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = value,
            onValueChange = onValueChange,
            shape = RoundedCornerShape(20.dp),
            trailingIcon = {
                RoundedIconButton(
                    icon = R.drawable.ic_send,
                    onClick = onSendIconClick,
                    enabled = isSendIconEnabled,
                    backgroundColor = sendIconColor,
                )
            },
            placeholder = {
                Text("Manda un mensaje...")
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                disabledBorderColor = Color.Transparent,
                containerColor = message_field_color
            ),
            enabled = isEnabled
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MessageFieldPreview() {
    CosasDeUnicorAppTheme(
        dynamicColor = false
    ) {
        MessageField(
            value = "",
            onValueChange = {},
        )
    }
}