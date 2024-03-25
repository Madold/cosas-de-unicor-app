package com.markusw.cosasdeunicorapp.tabulator.presentation.composables

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import com.markusw.cosasdeunicorapp.core.utils.TextUtils

@Composable
fun ScoreField(
    value: String,
    label: @Composable () -> Unit,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {

    OutlinedTextField(
        value = value,
        onValueChange = {
            if (TextUtils.isPositiveInteger(it) && it.toInt() <= 100) {
                onValueChange(it.toInt())
            }
        },
        label = label,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = modifier
    )

}