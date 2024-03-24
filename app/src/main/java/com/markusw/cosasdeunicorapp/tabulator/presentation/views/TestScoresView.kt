package com.markusw.cosasdeunicorapp.tabulator.presentation.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.markusw.cosasdeunicorapp.tabulator.presentation.TabulatorEvent
import com.markusw.cosasdeunicorapp.tabulator.presentation.TabulatorState
import com.markusw.cosasdeunicorapp.tabulator.presentation.TestType
import com.markusw.cosasdeunicorapp.tabulator.presentation.composables.After2014TestForm
import com.markusw.cosasdeunicorapp.tabulator.presentation.composables.Before2005TestForm
import com.markusw.cosasdeunicorapp.tabulator.presentation.composables.Before2014TestForm
import com.markusw.cosasdeunicorapp.ui.theme.CosasDeUnicorAppTheme

@Composable
fun TestScoresView(
    state: TabulatorState,
    onEvent: (TabulatorEvent) -> Unit = { }
) {

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Digita tus puntajes",
            style = MaterialTheme.typography.titleLarge
        )

        when (state.selectedTestType) {
            TestType.After2005 -> {
                Before2014TestForm(
                    state = state,
                    onEvent = onEvent,
                    modifier = Modifier.fillMaxSize()
                )
            }

            TestType.After2014 -> {
                After2014TestForm(
                    state = state,
                    onEvent = onEvent,
                    modifier = Modifier.fillMaxSize()
                )
            }

            TestType.Before2005 -> {
                Before2005TestForm(
                    state = state,
                    onEvent = onEvent,
                    modifier = Modifier.fillMaxSize()
                )
            }

        }
    }

}

@Preview(showBackground = true)
@Composable
fun TestScoresViewPreview() {
    CosasDeUnicorAppTheme {
        TestScoresView(
            state = TabulatorState(),
            onEvent = { }
        )
    }
}