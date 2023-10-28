package com.markusw.cosasdeunicorapp.home.presentation.docs

import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.markusw.cosasdeunicorapp.core.ext.showInterstitialAd
import com.markusw.cosasdeunicorapp.core.presentation.AdmobBanner
import com.markusw.cosasdeunicorapp.home.presentation.HomeUiEvent
import timber.log.Timber

@Composable
fun DocsScreenContent(
    onEvent: (HomeUiEvent) -> Unit,
    multiplePermissionLauncher: ManagedActivityResultLauncher<Array<String>, Map<String, Boolean>> = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = {}
    )
) {

    val documentSections = listOf(
        DocumentSection.ConsejoAcademico,
        DocumentSection.Admisiones
    )
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    Scaffold(
        content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(horizontal = 16.dp)
                    .verticalScroll(scrollState)
            ) {
                documentSections.forEach { section ->
                    Accordion(
                        title = section.label,
                        content = {
                            section.documents.forEach { documentReference ->
                                AccordionItem(
                                    title = documentReference.name,
                                    onItemClick = {
                                        context.showInterstitialAd(
                                            onAdDismissed = {
                                                onEvent(HomeUiEvent.DownloadDocument(documentReference.documentName))
                                            }
                                        )
                                    }
                                )
                            }
                        }
                    )
                }
            }
        },
        bottomBar = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                AdmobBanner()
            }
        }
    )


}