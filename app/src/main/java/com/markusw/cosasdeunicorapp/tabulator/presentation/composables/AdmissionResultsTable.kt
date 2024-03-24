package com.markusw.cosasdeunicorapp.tabulator.presentation.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.markusw.cosasdeunicorapp.core.utils.TextUtils
import com.markusw.cosasdeunicorapp.tabulator.domain.model.AdmissionResult

@Composable
fun AdmissionResultsTable(
    data: List<AdmissionResult>,
    modifier: Modifier = Modifier,
    firstColumnWeight: Float = 0.3f,
    secondColumnWeight: Float = 0.7f
) {

    LazyColumn(modifier = modifier
        .padding(16.dp)) {
        item {
            Row(Modifier.background(Color.Gray)) {
                TableCell(text = "Carrera", weight = firstColumnWeight)
                TableCell(text = "Porcentaje de ser admitido", weight = secondColumnWeight)
            }
        }
        items(data) { admissionResult ->
            Row(Modifier.fillMaxWidth()) {
                TableCell(text = admissionResult.academicProgram.name, weight = firstColumnWeight)
                TableCell(text = TextUtils.formatPercents(admissionResult.admissionPercentage), weight = secondColumnWeight)
            }
        }
    }

}

@Composable
fun RowScope.TableCell(
    text: String,
    weight: Float
) {
    Text(
        text = text,
        Modifier
            .border(1.dp, Color.Black)
            .weight(weight)
            .padding(8.dp)
    )
}