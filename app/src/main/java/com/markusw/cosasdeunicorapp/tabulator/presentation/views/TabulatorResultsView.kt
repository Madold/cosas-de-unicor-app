package com.markusw.cosasdeunicorapp.tabulator.presentation.views

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.markusw.cosasdeunicorapp.tabulator.presentation.TabulatorEvent
import com.markusw.cosasdeunicorapp.tabulator.presentation.TabulatorState

@Composable
fun TabulatorResultsView(
    state: TabulatorState,
    onEvent: (TabulatorEvent) -> Unit = { }
) {
    Text("Results view")
}