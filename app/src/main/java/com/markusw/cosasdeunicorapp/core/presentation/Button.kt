package com.markusw.cosasdeunicorapp.core.presentation

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.markusw.cosasdeunicorapp.ui.theme.CosasDeUnicorAppTheme
import com.markusw.cosasdeunicorapp.ui.theme.md_theme_light_primary
import androidx.compose.material3.Button as Material3Button

/*
* App design system button
* */
@Composable
fun Button(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    enabled: Boolean = true,
    shape: Shape = RoundedCornerShape(8.dp),
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    elevation: ButtonElevation? = ButtonDefaults.buttonElevation(),
    content: @Composable () -> Unit,
) {

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val animatedScale by animateFloatAsState(
        targetValue = if (isPressed) 0.92f else 1f,
        label = "buttonScale"
    )
    //val context = LocalContext.current


    Material3Button(
        onClick = onClick,
        modifier = modifier
            .scale(animatedScale),
        interactionSource = interactionSource,
        colors = colors,
        enabled = enabled,
        shape = shape,
        contentPadding = contentPadding,
        elevation = elevation
    ) {
       Row(
           horizontalArrangement = Arrangement.spacedBy(8.dp),
           verticalAlignment = Alignment.CenterVertically
       ) {
           content()
       }
    }

}

@Preview(showBackground = true)
@Composable
fun ButtonPreview() {
    CosasDeUnicorAppTheme(
        darkTheme = false
    ) {
        Button(
            onClick = { /*TODO*/ },
            content = { Text(text = "I'm a button") },
            colors = ButtonDefaults.buttonColors(
                containerColor = md_theme_light_primary
            )
        )
    }
}