package com.markusw.cosasdeunicorapp.tabulator.presentation.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.markusw.cosasdeunicorapp.tabulator.presentation.TabulatorEvent
import com.markusw.cosasdeunicorapp.tabulator.presentation.TabulatorState
import com.markusw.cosasdeunicorapp.tabulator.presentation.TestType
import com.markusw.cosasdeunicorapp.tabulator.presentation.composables.TestTypeItem
import com.markusw.cosasdeunicorapp.ui.theme.CosasDeUnicorAppTheme

@Composable
fun TestTypeSelectionView(
    state: TabulatorState,
    onEvent: (TabulatorEvent) -> Unit = { }
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Selecciona tu tipo de prueba",
            style = MaterialTheme.typography.titleLarge
        )

        TestType.entries.forEach { testType ->

            val isSelected = state.selectedTestType == testType
            val chipContainerColor = if (isSelected) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surface
            }

            TestTypeItem(
                testType = testType,
                isSelected = isSelected,
                onClick = {
                    onEvent(TabulatorEvent.ChangeSelectedTestType(testType))
                }
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
fun TestTypeSelectionViewPreview() {

    CosasDeUnicorAppTheme(
        darkTheme = true
    ) {
        TestTypeSelectionView(
            state = TabulatorState(),
            onEvent = { }
        )
    }

}