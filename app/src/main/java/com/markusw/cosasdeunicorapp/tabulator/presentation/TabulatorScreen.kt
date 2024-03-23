@file:OptIn(ExperimentalLayoutApi::class)

package com.markusw.cosasdeunicorapp.tabulator.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.markusw.cosasdeunicorapp.core.presentation.AppTopBar
import com.markusw.cosasdeunicorapp.core.presentation.Button
import com.markusw.cosasdeunicorapp.tabulator.domain.Before2014
import com.markusw.cosasdeunicorapp.tabulator.presentation.composables.After2014TestForm
import com.markusw.cosasdeunicorapp.tabulator.presentation.composables.Before2005TestForm
import com.markusw.cosasdeunicorapp.tabulator.presentation.composables.Before2014TestForm
import com.markusw.cosasdeunicorapp.ui.theme.CosasDeUnicorAppTheme

@Composable
fun TabulatorScreen(
    mainNavController: NavController,
    state: TabulatorState,
    onEvent: (TabulatorEvent) -> Unit = { }
) {
    Scaffold(
        topBar = {
            AppTopBar(
                title = {
                    Text("Tabulador")
                },
                navigationIcon = {
                    IconButton(onClick = { mainNavController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Button(onClick = {  }, modifier = Modifier.fillMaxWidth(0.9f)) {
                    Text(text = "Calcular")
                }
            }
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Selecciona tu tipo de prueba",
                    style = MaterialTheme.typography.titleLarge
                )

                FlowRow(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TestType.entries.forEach { testType ->

                        val isSelected = state.selectedTestType == testType
                        val chipContainerColor = if (isSelected) {
                            MaterialTheme.colorScheme.primaryContainer
                        } else {
                            MaterialTheme.colorScheme.surface
                        }

                        AssistChip(
                            onClick = { onEvent(TabulatorEvent.ChangeSelectedTestType(testType)) },
                            label = {
                                Text(testType.label)
                            },
                            colors = AssistChipDefaults.assistChipColors(
                                containerColor = chipContainerColor
                            )
                        )
                    }
                }

                Text(
                    text = "Digita tus puntajes",
                    style = MaterialTheme.typography.titleLarge
                )

                when (state.selectedTestType) {
                    TestType.After2005 -> {
                        Before2014TestForm(
                            state = state,
                            onEvent = onEvent,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    TestType.After2014 -> {
                        After2014TestForm(
                            state = state,
                            onEvent = onEvent,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    TestType.Before2005 -> {
                        Before2005TestForm(
                            state = state,
                            onEvent = onEvent,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    )
}

@Preview(
    showBackground = true
)
@Composable
fun TabulatorScreenPreview() {

    CosasDeUnicorAppTheme(
        darkTheme = true
    ) {
        TabulatorScreen(
            mainNavController = rememberNavController(),
            state = TabulatorState()
        )
    }
}