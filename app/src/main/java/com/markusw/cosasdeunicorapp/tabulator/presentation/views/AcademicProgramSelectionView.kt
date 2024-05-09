package com.markusw.cosasdeunicorapp.tabulator.presentation.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.markusw.cosasdeunicorapp.R
import com.markusw.cosasdeunicorapp.tabulator.presentation.TabulatorEvent
import com.markusw.cosasdeunicorapp.tabulator.presentation.TabulatorState
import com.markusw.cosasdeunicorapp.tabulator.presentation.composables.AcademicProgramItem
import com.markusw.cosasdeunicorapp.ui.theme.CosasDeUnicorAppTheme
import timber.log.Timber

@Composable
fun AcademicProgramSelectionView(
    state: TabulatorState,
    onEvent: (TabulatorEvent) -> Unit = { }
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(
            text = "Selecciona el programa académico al que deseas aplicar",
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center
        )

        OutlinedTextField(
            value = state.academicProgramName,
            onValueChange = { onEvent(TabulatorEvent.ChangeAcademicProgramName(it)) },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_search),
                    contentDescription = null
                )
            },
            placeholder = {
                Text("Escribe el nombre de un programa")
            }
        )

        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(state.academicPrograms) { academicProgram ->
                AcademicProgramItem(
                    academicProgram = academicProgram,
                    onClick = { onEvent(TabulatorEvent.ChangeSelectedAcademicProgram(it)) },
                    isSelected = state.selectedAcademicProgram == academicProgram
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AcademicProgramSelectionViewPreview() {
     CosasDeUnicorAppTheme(
         darkTheme = true
     ) {
         AcademicProgramSelectionView(
             state = TabulatorState(),
             onEvent = {}
         )
     }
}