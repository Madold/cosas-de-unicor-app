package com.markusw.cosasdeunicorapp.tabulator.presentation.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.markusw.cosasdeunicorapp.core.utils.TextUtils
import com.markusw.cosasdeunicorapp.tabulator.domain.model.AcademicProgram
import com.markusw.cosasdeunicorapp.tabulator.domain.model.AdmissionResult

@Composable
fun AdmissionResultsTable(
    data: List<AdmissionResult>,
    selectedAcademicProgram: AcademicProgram,
    modifier: Modifier = Modifier,
    firstColumnWeight: Float = 0.3f,
    secondColumnWeight: Float = 0.7f,
    thirdColumnWeight: Float = 0.0f,
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
                    text = "Carrera",
                    weight = firstColumnWeight,
                    textColor = MaterialTheme.colorScheme.onPrimary,
                )
                TableCell(
                    text = "Porcentaje de admisión",
                    weight = secondColumnWeight,
                    textColor = MaterialTheme.colorScheme.onPrimary,
                )
                TableCell(
                    text = "Puntaje en la Universidad",
                    weight = thirdColumnWeight,
                    textColor = MaterialTheme.colorScheme.onPrimary,
                )
            }
        }
        items(data) { admissionResult ->

            val isHighlighted = admissionResult.academicProgram == selectedAcademicProgram

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        if (isHighlighted) Color(0xFF009432) else Color.Transparent
                    )
                ,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TableCell(
                    text = admissionResult.academicProgram.name,
                    weight = firstColumnWeight
                )
                TableCell(
                    text = TextUtils.formatPercents(admissionResult.admissionPercentage),
                    weight = secondColumnWeight
                )
                TableCell(
                    text = admissionResult.weighted.toString(),
                    weight = secondColumnWeight
                )
            }

            Divider()
        }
    }

}

@Composable
fun RowScope.TableCell(
    text: String,
    weight: Float,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    textAlign: TextAlign = TextAlign.Start
) {
    Text(
        text = text,
        Modifier
            .weight(weight)
            .padding(8.dp),
        color = textColor,
        textAlign = textAlign
    )
}