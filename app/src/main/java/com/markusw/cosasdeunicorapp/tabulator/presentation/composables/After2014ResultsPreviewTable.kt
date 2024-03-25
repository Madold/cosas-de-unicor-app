package com.markusw.cosasdeunicorapp.tabulator.presentation.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.markusw.cosasdeunicorapp.tabulator.presentation.TabulatorState

@Composable
fun After2014ResultsPreviewTable(
    state: TabulatorState,
    modifier: Modifier = Modifier,
    firstColumnWeight: Float = 0.3f,
    secondColumnWeight: Float = 0.7f,
) {
    LazyColumn(
        modifier = modifier
            .clip(RoundedCornerShape(15.dp))
            .border(
                1.dp,
                MaterialTheme.colorScheme.onSurface,
                shape = RoundedCornerShape(15.dp)
            )
    ) {
        item {
            Row(
                Modifier.background(MaterialTheme.colorScheme.primary),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                TableCell(
                    text = "Area",
                    weight = firstColumnWeight,
                    textColor = MaterialTheme.colorScheme.onPrimary,
                )
                TableCell(
                    text = "Puntaje",
                    weight = secondColumnWeight,
                    textColor = MaterialTheme.colorScheme.onPrimary,
                )
            }
        }

        item {
            Row(
                Modifier.background(MaterialTheme.colorScheme.surface),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                TableCell(
                    text = "Lectura Crítica",
                    weight = firstColumnWeight,
                    textColor = MaterialTheme.colorScheme.onSurface,
                )
                TableCell(
                    text = state.criticalReadingScore.toString(),
                    weight = secondColumnWeight,
                    textColor = MaterialTheme.colorScheme.onSurface,
                )
            }
        }

        item {
            Row(
                Modifier.background(MaterialTheme.colorScheme.surface),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                TableCell(
                    text = "Ciencias Naturales",
                    weight = firstColumnWeight,
                    textColor = MaterialTheme.colorScheme.onSurface,
                )
                TableCell(
                    text = state.naturalSciencesScore.toString(),
                    weight = secondColumnWeight,
                    textColor = MaterialTheme.colorScheme.onSurface,
                )
            }
        }

        item {
            Row(
                Modifier.background(MaterialTheme.colorScheme.surface),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                TableCell(
                    text = "Ciencias Sociales",
                    weight = firstColumnWeight,
                    textColor = MaterialTheme.colorScheme.onSurface,
                )
                TableCell(
                    text = state.socialSciencesScore.toString(),
                    weight = secondColumnWeight,
                    textColor = MaterialTheme.colorScheme.onSurface,
                )
            }
        }

        item {
            Row(
                Modifier.background(MaterialTheme.colorScheme.surface),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                TableCell(
                    text = "Matemáticas",
                    weight = firstColumnWeight,
                    textColor = MaterialTheme.colorScheme.onSurface,
                )
                TableCell(
                    text = state.mathScore.toString(),
                    weight = secondColumnWeight,
                    textColor = MaterialTheme.colorScheme.onSurface,
                )
            }
        }

        item {
            Row(
                Modifier.background(MaterialTheme.colorScheme.surface),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                TableCell(
                    text = "Inglés",
                    weight = firstColumnWeight,
                    textColor = MaterialTheme.colorScheme.onSurface,
                )
                TableCell(
                    text = state.englishScore.toString(),
                    weight = secondColumnWeight,
                    textColor = MaterialTheme.colorScheme.onSurface,
                )
            }
        }
    }
}