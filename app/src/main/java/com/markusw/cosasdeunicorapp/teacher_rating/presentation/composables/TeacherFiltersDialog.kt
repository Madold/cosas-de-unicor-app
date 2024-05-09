@file:OptIn(
    ExperimentalLayoutApi::class,
    ExperimentalMaterial3Api::class
)

package com.markusw.cosasdeunicorapp.teacher_rating.presentation.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.markusw.cosasdeunicorapp.core.presentation.Button
import com.markusw.cosasdeunicorapp.teacher_rating.domain.model.TeacherRating
import com.markusw.cosasdeunicorapp.teacher_rating.presentation.FilterType
import com.markusw.cosasdeunicorapp.ui.theme.homie_color
import com.markusw.cosasdeunicorapp.ui.theme.pushy_color
import com.markusw.cosasdeunicorapp.ui.theme.ruthless_color
import com.markusw.cosasdeunicorapp.ui.theme.supportive_color

const val AZ = "A-Z"
const val ZA = "Z-A"

@Composable
fun TeacherFiltersDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    onChipClick: (TeacherRating) -> Unit,
    selectedFilterType: FilterType,
    selectedOption: String,
    isDropDownMenuExpanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onDropDownMenuOptionChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {

    val options = listOf(AZ, ZA)

    Dialog(
        onDismissRequest = onDismiss,
        content = {
            Card(
                modifier = modifier
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Filtros",
                        style = MaterialTheme.typography.titleMedium.copy(textAlign = TextAlign.Center),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text("Ordenar por nombre")

                    ExposedDropdownMenuBox(
                        expanded = isDropDownMenuExpanded,
                        onExpandedChange = onExpandedChange,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = selectedOption,
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = {
                                IconButton(onClick = { onExpandedChange(true) }) {
                                    Icon(
                                        imageVector = Icons.Default.ArrowDropDown,
                                        contentDescription = null,
                                        modifier = Modifier.rotate(if (isDropDownMenuExpanded) 180f else 0f)
                                    )
                                }
                            },
                            modifier = Modifier
                                .menuAnchor()
                        )

                        ExposedDropdownMenu(
                            expanded = isDropDownMenuExpanded,
                            onDismissRequest = { onExpandedChange(false) }
                        ) {
                            options.forEach { option ->
                                DropdownMenuItem(
                                    text = {
                                        Text(text = option)
                                    },
                                    onClick = {
                                        onDropDownMenuOptionChange(option)
                                        onExpandedChange(false)
                                    }
                                )
                            }
                        }
                    }

                    Text("Valoración del profesor")
                    FlowRow(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        TeacherRating.entries.forEach { rating ->
                            when (rating) {
                                TeacherRating.Homie -> {
                                    RatingChip(
                                        onClick = {
                                            onChipClick(rating)
                                        },
                                        label = {
                                            Text(
                                                text = "Valecita",
                                                color = Color.White
                                            )
                                        },
                                        backgroundColor = homie_color,
                                        selected = selectedFilterType == FilterType.ByRating(rating)
                                    )
                                }

                                TeacherRating.Ruthless -> {
                                    RatingChip(
                                        onClick = {
                                            onChipClick(rating)
                                        },
                                        label = {
                                            Text(text = "Cuchilla",
                                                color = Color.White
                                            )
                                        },
                                        backgroundColor = ruthless_color,
                                        selected = selectedFilterType == FilterType.ByRating(rating)
                                    )
                                }

                                TeacherRating.Pushy -> {
                                    RatingChip(
                                        onClick = {
                                            onChipClick(rating)
                                        },
                                        label = {
                                            Text(text = "Pesao",
                                                color = Color.White
                                            )
                                        },
                                        backgroundColor = pushy_color,
                                        selected = selectedFilterType == FilterType.ByRating(rating)
                                    )
                                }

                                TeacherRating.Supportive -> {
                                    RatingChip(
                                        onClick = {
                                            onChipClick(rating)
                                        },
                                        label = {
                                            Text(text = "Calidoso",
                                                color = Color.White
                                            )
                                        },
                                        backgroundColor = supportive_color,
                                        selected = selectedFilterType == FilterType.ByRating(rating)
                                    )
                                }
                            }
                        }
                    }
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(onClick = onConfirm) {
                            Text(text = "Aplicar")
                        }
                    }
                    
                }
            }
        }
    )
}