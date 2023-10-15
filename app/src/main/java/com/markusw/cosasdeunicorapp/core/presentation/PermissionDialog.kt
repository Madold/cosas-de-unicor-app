package com.markusw.cosasdeunicorapp.core.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.markusw.cosasdeunicorapp.R

@Composable
fun PermissionDialog(
    permissionTextProvider: PermissionTextProvider,
    isPermanentlyDeclined: Boolean,
    onDismiss: () -> Unit,
    onDone: () -> Unit,
    onGotToSettings: () -> Unit,
    modifier: Modifier = Modifier
) {

    Dialog(
        onDismissRequest = onDismiss,
        content = {
            Surface(
                modifier = modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    PermissionHeader(text = stringResource(id = R.string.permission_required))
                    Divider(modifier = Modifier.fillMaxWidth())
                    PermissionBody(
                        text = permissionTextProvider.getDescription(
                            isPermanentlyDeclined
                        ).asString()
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        contentAlignment = Alignment.BottomEnd
                    ) {
                        TextButton(
                            onClick = { if (isPermanentlyDeclined) onGotToSettings() else onDone() },
                            shape = RoundedCornerShape(size = 4.dp),
                            content = {
                                Text(
                                    text = if (isPermanentlyDeclined) stringResource(R.string.grant_permission)
                                    else stringResource(R.string.OK)
                                )
                            }
                        )
                    }
                }

            }
        }
    )
}

@Composable
fun PermissionHeader(
    text: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
    ) {
        Text(
            text = text
        )
    }
}

@Composable
fun PermissionBody(
    text: String,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        Text(text = text)
    }
}