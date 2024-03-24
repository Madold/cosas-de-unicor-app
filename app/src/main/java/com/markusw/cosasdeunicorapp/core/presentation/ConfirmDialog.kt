package com.markusw.cosasdeunicorapp.core.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.markusw.cosasdeunicorapp.ui.theme.CosasDeUnicorAppTheme

@Composable
fun ConfirmDialog(
    title: @Composable () -> Unit,
    body: @Composable () -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {

    Dialog(
        onDismissRequest = onDismiss,
        content = {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = modifier
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    title()
                    body()
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            onClick = onDismiss,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Text("Cancelar")
                        }

                        Button(onClick = onConfirm) {
                            Text("Confirmar")
                        }
                    }
                }
            }
        }
    )
}

@Preview
@Composable
fun ConfirmDialogPreview() {
    CosasDeUnicorAppTheme(
        darkTheme = true
    ) {
        ConfirmDialog(
            title = {
                Text("Título")
            },
            body = {
                Text("Cuerpo")
            },
            onConfirm = {  },
            onDismiss = {  }
        )
    }
}