@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.markusw.cosasdeunicorapp.home.chat.composables

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.markusw.cosasdeunicorapp.R
import com.markusw.cosasdeunicorapp.ui.theme.CosasDeUnicorAppTheme
import com.markusw.cosasdeunicorapp.ui.theme.md_theme_dark_primary
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
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .run {
                        if (isSendIconEnabled) clickable {
                            onSendIconClick()
                        }
                        else this
                    }
                    .background(sendIconColor)
                    .padding(all = 10.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_send),
                    contentDescription = "Send",
                    tint = Color.White
                )
            }
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