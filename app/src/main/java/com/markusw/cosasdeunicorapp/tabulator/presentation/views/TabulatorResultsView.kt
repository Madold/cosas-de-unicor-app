package com.markusw.cosasdeunicorapp.tabulator.presentation.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markusw.cosasdeunicorapp.tabulator.presentation.TabulatorEvent
import com.markusw.cosasdeunicorapp.tabulator.presentation.TabulatorState
import com.markusw.cosasdeunicorapp.tabulator.presentation.composables.AdmissionResultsTable

@Composable
fun TabulatorResultsView(
    state: TabulatorState,
    onEvent: (TabulatorEvent) -> Unit = { }
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(
            text = "Resultados",
            style = MaterialTheme.typography.titleLarge
        )

        AdmissionResultsTable(
            data = state.admissionResults,
            firstColumnWeight = 0.5f,
            secondColumnWeight = 0.5f,
            modifier = Modifier.fillMaxSize()
        )

    }
}