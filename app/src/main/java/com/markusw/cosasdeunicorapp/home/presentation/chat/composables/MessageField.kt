@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.markusw.cosasdeunicorapp.home.presentation.chat.composables

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
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
) {

    val sendIconColor by animateColorAsState(
        targetValue = if (isSendIconEnabled) md_theme_light_primary else md_theme_light_primary.copy(alpha = 0.5f),
        label = ""
    )


    OutlinedTextField(
        modifier = modifier,
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