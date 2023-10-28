package com.markusw.cosasdeunicorapp.home.presentation.docs

import android.Manifest
import android.os.Build
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.markusw.cosasdeunicorapp.core.ext.isPermissionGranted
import com.markusw.cosasdeunicorapp.core.ext.showInterstitialAd
import com.markusw.cosasdeunicorapp.core.presentation.AdmobBanner
import com.markusw.cosasdeunicorapp.home.presentation.HomeState
import com.markusw.cosasdeunicorapp.home.presentation.HomeUiEvent
import com.markusw.cosasdeunicorapp.home.presentation.permissionsToRequest

@Composable
fun DocsScreenContent(
    state: HomeState,
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

    fun handleOnDownloadDocument(documentName: String) {

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {

            val arePermissionsGranted =
                context.isPermissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE) || context.isPermissionGranted(
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )

            if (!arePermissionsGranted) {
                multiplePermissionLauncher.launch(permissionsToRequest)
                return
            }

            context.showInterstitialAd(
                onAdDismissed = {
                    onEvent(
                        HomeUiEvent.DownloadDocument(
                            documentName
                        )
                    )
                }
            )

        } else {
            context.showInterstitialAd(
                onAdDismissed = {
                    onEvent(
                        HomeUiEvent.DownloadDocument(
                            documentName
                        )
                    )
                }
            )
        }

    }

    Scaffold(
        content = { padding ->
            Box(
                Modifier.padding(padding)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
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
                                        onItemClick = { handleOnDownloadDocument(documentReference.documentName) }
                                    )
                                }
                            }
                        )
                    }
                }

                Column(
                    modifier = Modifier.fillMaxSize(),
                ) {

                    AnimatedVisibility(
                        visible = state.isDownloadingDocument,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(color = MaterialTheme.colorScheme.scrim.copy(alpha = 0.5f)),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

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