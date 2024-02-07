package com.markusw.cosasdeunicorapp.core.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.markusw.cosasdeunicorapp.R
import com.markusw.cosasdeunicorapp.ui.theme.CosasDeUnicorAppTheme

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
                    .clip(RoundedCornerShape(12.dp)),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    PermissionHeader(
                        text = stringResource(id = R.string.permission_required),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Divider(modifier = Modifier.fillMaxWidth())
                    PermissionBody(
                        text = permissionTextProvider.getDescription(
                            isPermanentlyDeclined
                        ).asString(),
                        isPermanentlyDeclined = isPermanentlyDeclined,
                        illustration = {
                            Image(
                                painter = painterResource(id = permissionTextProvider.getIllustration()),
                                contentDescription = null
                            )
                        }
                    )
                    Button(
                        onClick = { if (isPermanentlyDeclined) onGotToSettings() else onDone() },
                        content = {
                            Text(
                                text = if (isPermanentlyDeclined) stringResource(R.string.grant_permission)
                                else stringResource(R.string.OK)
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                    )
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
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun PermissionBody(
    text: String,
    isPermanentlyDeclined: Boolean,
    modifier: Modifier = Modifier,
    illustration: @Composable () -> Unit = {},
) {
    Column(modifier = modifier) {
        if (!isPermanentlyDeclined) {
            illustration()
        }
        Text(text = text, textAlign = TextAlign.Center)
    }

}

@Preview(showBackground = true)
@Composable
fun PermissionDialogPreview() {
    CosasDeUnicorAppTheme {
        PermissionDialog(
            permissionTextProvider = ReadExternalStoragePermission(),
            isPermanentlyDeclined = true,
            onDismiss = { },
            onDone = { },
            onGotToSettings = { })
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4_XL, showSystemUi = true)
@Composable
fun PermissionDialogPreviewInDevice() {
    CosasDeUnicorAppTheme {
        Scaffold { padding ->
            Box(
                modifier = Modifier
                    .padding(padding)
            ) {
                PermissionDialog(
                    permissionTextProvider = WriteExternalStoragePermission(),
                    isPermanentlyDeclined = false,
                    onDismiss = { },
                    onDone = { },
                    onGotToSettings = { }
                )
            }
        }
    }
}