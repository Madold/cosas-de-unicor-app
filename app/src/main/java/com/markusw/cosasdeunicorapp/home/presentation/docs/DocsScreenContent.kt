package com.markusw.cosasdeunicorapp.home.presentation.docs

import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.markusw.cosasdeunicorapp.home.presentation.HomeUiEvent

@Composable
fun DocsScreenContent(
    onEvent: (HomeUiEvent) -> Unit,
    multiplePermissionLauncher: ManagedActivityResultLauncher<Array<String>, Map<String, Boolean>> = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = {}
    )
) {

    val context = LocalContext.current
    val documentSections = listOf(
        DocumentSection.ConsejoAcademico,
        DocumentSection.Admisiones
    )
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        /*
        Button(onClick = {

            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    onEvent(HomeUiEvent.DownloadDocument("puntajes_referencia_2022.pdf"))

                    return@Button
                }

                multiplePermissionLauncher.launch(permissionsToRequest)
            } else {
                onEvent(HomeUiEvent.DownloadDocument("requisitos_doble_programa.docx"))
            }

        }) {
            Text(text = "Descargar documento")
        }*/
        
        documentSections.forEach { section ->
            Accordion(
                title = section.label,
                content = {
                    section.documents.forEach { documentReference ->
                        AccordionItem(
                            title = documentReference.name,
                            onItemClick = {
                                onEvent(HomeUiEvent.DownloadDocument(documentReference.documentName))
                            }
                        )
                    }
                }
            )
        }
        
    }
}