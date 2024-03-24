package com.markusw.cosasdeunicorapp.tabulator.presentation.composables

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.markusw.cosasdeunicorapp.R
import com.markusw.cosasdeunicorapp.tabulator.domain.model.AcademicProgram

@Composable
fun AcademicProgramItem(
    academicProgram: AcademicProgram,
    onClick: (AcademicProgram) -> Unit,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
) {

    NavigationDrawerItem(
        label = { Text(academicProgram.name) },
        selected = isSelected,
        onClick = { onClick(academicProgram) },
        modifier = modifier,
        shape = RoundedCornerShape(15.dp),
        badge = {
            if (isSelected) {
                Icon(painter = painterResource(id = R.drawable.ic_check), contentDescription = null)
            }
        }
    )

}