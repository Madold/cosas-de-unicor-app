package com.markusw.cosasdeunicorapp.tabulator.presentation.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markusw.cosasdeunicorapp.tabulator.presentation.TabulatorEvent
import com.markusw.cosasdeunicorapp.tabulator.presentation.TabulatorState

@Composable
fun Before2014TestForm(
    state: TabulatorState,
    modifier: Modifier = Modifier,
    onEvent: (TabulatorEvent) -> Unit = { },
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Adaptive(128.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {

        item {
            ScoreField(
                value = state.chemistryScore.toString(),
                label = {
                    Text("Química")
                },
                onValueChange = { onEvent(TabulatorEvent.ChangeChemistryScore(it)) },
            )
        }

        item {
            ScoreField(
                value = state.physicsScore.toString(),
                label = {
                    Text("Física")
                },
                onValueChange = { onEvent(TabulatorEvent.ChangePhysicsScore(it)) },
            )
        }

        item {
            ScoreField(
                value = state.biologyScore.toString(),
                label = {
                    Text("Biología")
                },
                onValueChange = { onEvent(TabulatorEvent.ChangeBiologyScore(it)) },
            )
        }

        item {
            ScoreField(
                value = state.spanishScore.toString(),
                label = {
                    Text("Español")
                },
                onValueChange = { onEvent(TabulatorEvent.ChangeSpanishScore(it)) },
            )
        }

        item {
            ScoreField(
                value = state.philosophyScore.toString(),
                label = {
                    Text("Filosofía")
                },
                onValueChange = { onEvent(TabulatorEvent.ChangePhilosophyScore(it)) },
            )
        }

        item {
            ScoreField(
                value = state.mathScore.toString(),
                label = {
                    Text("Matemáticas")
                },
                onValueChange = { onEvent(TabulatorEvent.ChangeMathScore(it)) },
            )
        }

        item {
            ScoreField(
                value = state.socialSciencesScore.toString(),
                label = {
                    Text("Ciencias Sociales")
                },
                onValueChange = { onEvent(TabulatorEvent.ChangeSocialSciencesScore(it)) },
            )
        }

        item {
            ScoreField(
                value = state.englishScore.toString(),
                label = {
                    Text("Inglés")
                },
                onValueChange = { onEvent(TabulatorEvent.ChangeEnglishScore(it)) },
            )
        }

    }
}