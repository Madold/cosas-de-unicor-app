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
import com.markusw.cosasdeunicorapp.tabulator.presentation.TestType

@Composable
fun TestTypeItem(
    testType: TestType,
    isSelected: Boolean,
    onClick: (TestType) -> Unit,
    modifier: Modifier = Modifier
) {

    NavigationDrawerItem(
        label = { Text(testType.label) },
        selected = isSelected,
        onClick = { onClick(testType) },
        modifier = modifier,
        shape = RoundedCornerShape(15.dp),
        badge = {
            if (isSelected) {
                Icon(painter = painterResource(id = R.drawable.ic_check), contentDescription = null)
            }
        }
    )

}