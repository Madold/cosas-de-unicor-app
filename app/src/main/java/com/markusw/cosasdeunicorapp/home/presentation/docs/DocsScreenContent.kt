package com.markusw.cosasdeunicorapp.home.presentation.docs

import android.Manifest
import android.os.Build
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.markusw.cosasdeunicorapp.R
import com.markusw.cosasdeunicorapp.core.ext.isPermissionGranted
import com.markusw.cosasdeunicorapp.core.ext.showInterstitialAd
import com.markusw.cosasdeunicorapp.core.presentation.AdmobBanner
import com.markusw.cosasdeunicorapp.core.presentation.AppTopBar
import com.markusw.cosasdeunicorapp.core.utils.AdUtils
import com.markusw.cosasdeunicorapp.core.utils.TextUtils
import com.markusw.cosasdeunicorapp.core.utils.TextUtils.DOCX
import com.markusw.cosasdeunicorapp.core.utils.TextUtils.PDF
import com.markusw.cosasdeunicorapp.core.utils.TextUtils.XLSX
import com.markusw.cosasdeunicorapp.home.presentation.HomeState
import com.markusw.cosasdeunicorapp.home.presentation.HomeUiEvent
import com.markusw.cosasdeunicorapp.home.presentation.permissionsToRequest
import com.markusw.cosasdeunicorapp.ui.theme.CosasDeUnicorAppTheme
import com.markusw.cosasdeunicorapp.ui.theme.home_bottom_bar_background

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocsScreenContent(
    state: HomeState,
    onEvent: (HomeUiEvent) -> Unit,
    multiplePermissionLauncher: ManagedActivityResultLauncher<Array<String>, Map<String, Boolean>> = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = {}
    )
) {

    val scrollState = rememberScrollState()
    val context = LocalContext.current

    fun handleOnDownloadDocument(documentName: String) {
        /**
         * If the device is running Android 10 or lower, we need to request the storage permissions
         * In android 11 or higher, the storage permissions are granted automatically when the app is installed
         */
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {

            val arePermissionsGranted =
                context.isPermissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE) && context.isPermissionGranted(
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )

            if (!arePermissionsGranted) {
                multiplePermissionLauncher.launch(permissionsToRequest)
                return
            }

            if (AdUtils.shouldShowInterstitialAd()) {
                context.showInterstitialAd(
                    onAdDismissed = {
                        onEvent(HomeUiEvent.DownloadDocument(documentName))
                    }
                )
                return
            }

            onEvent(HomeUiEvent.DownloadDocument(documentName))
        } else {
            if (AdUtils.shouldShowInterstitialAd()) {
                context.showInterstitialAd(
                    onAdDismissed = {
                        onEvent(HomeUiEvent.DownloadDocument(documentName))
                    }
                )
                return
            }

            onEvent(HomeUiEvent.DownloadDocument(documentName))
        }

    }

    Scaffold(
        topBar = {
            Box {
                AppTopBar(
                    title = {
                        Text(
                            text = "Formatos",
                            color = Color.White,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                    },
                    actions = {
                        IconButton(onClick = { onEvent(HomeUiEvent.ChangeSearchBarActive(true)) }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_search),
                                contentDescription = null,
                                tint = Color.White
                            )
                        }
                    }
                )
                SearchBar(
                    query = state.documentName,
                    onQueryChange = {
                        onEvent(HomeUiEvent.ChangeDocumentName(it))
                        onEvent(HomeUiEvent.SearchDocument(it))
                    },
                    onSearch = { onEvent(HomeUiEvent.SearchDocument(it)) },
                    active = state.isDocumentSearchBarActive,
                    onActiveChange = { onEvent(HomeUiEvent.ChangeSearchBarActive(it)) },
                    modifier = Modifier.run {
                        if (state.isDocumentSearchBarActive) {
                            this.fillMaxWidth()
                        } else {
                            this.width(0.dp)
                        }
                    },
                    colors = SearchBarDefaults.colors(
                        containerColor = home_bottom_bar_background,
                        inputFieldColors = SearchBarDefaults.inputFieldColors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    ),
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_search),
                            contentDescription = null,
                            tint = Color.White
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = {
                            onEvent(HomeUiEvent.ChangeDocumentName(""))
                            onEvent(HomeUiEvent.ChangeSearchBarActive(false))
                        }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = null,
                                tint = Color.White
                            )
                        }
                    }
                ) {
                    LazyColumn {
                        items(state.searchedDocumentsList) { document ->
                            val documentCover = when (TextUtils.getFileExtensionFromName(
                                document.documentName
                            )) {
                                PDF -> R.drawable.pdf_icon
                                XLSX -> R.drawable.excel_icon
                                DOCX -> R.drawable.word_icon
                                else -> R.drawable.file_icon
                            }


                            AccordionItem(
                                label = {
                                    Text(text = document.name)
                                },
                                onItemClick = {
                                    handleOnDownloadDocument(
                                        document.documentName
                                    )
                                },
                                icon = {
                                    Box(
                                        modifier = Modifier
                                            .clip(CircleShape)
                                            .background(Color.White)
                                            .padding(5.dp)
                                    ) {
                                        Image(
                                            painter = painterResource(id = documentCover),
                                            contentDescription = null,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                },
                                colors = NavigationDrawerItemDefaults.colors(
                                    unselectedContainerColor = Color.Transparent,
                                    unselectedTextColor = Color.White
                                )
                            )
                        }
                    }
                }
            }
        },
        content = { padding ->
            Box(
                Modifier.padding(padding)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                ) {
                    Spacer(modifier = Modifier.height(8.dp))
                    state.documentCategories.forEach { category ->

                        val categoryIcon = when (category) {
                            "Consejo Académico" -> R.drawable.ic_academic
                            "Registro y admisiones" -> R.drawable.ic_registration
                            else -> R.drawable.ic_documents
                        }

                        Accordion(
                            modifier = Modifier.fillMaxWidth(),
                            label = {
                                Text(
                                    text = category,
                                    style = MaterialTheme.typography.titleMedium
                                )
                            },
                            content = {
                                state
                                    .documentsList
                                    .filter { it.category == category }
                                    .forEach { documentReference ->

                                        val documentCover = remember {
                                            when (TextUtils.getFileExtensionFromName(
                                                documentReference.documentName
                                            )) {
                                                PDF -> R.drawable.pdf_icon
                                                XLSX -> R.drawable.excel_icon
                                                DOCX -> R.drawable.word_icon
                                                else -> R.drawable.file_icon
                                            }
                                        }

                                        AccordionItem(
                                            label = {
                                                Text(text = documentReference.name)
                                            },
                                            onItemClick = {
                                                handleOnDownloadDocument(
                                                    documentReference.documentName
                                                )
                                            },
                                            icon = {
                                                Box(
                                                    modifier = Modifier
                                                        .clip(CircleShape)
                                                        .background(Color.White)
                                                        .padding(5.dp)
                                                ) {
                                                    Image(
                                                        painter = painterResource(id = documentCover),
                                                        contentDescription = null,
                                                        modifier = Modifier.size(24.dp)
                                                    )
                                                }
                                            }
                                        )
                                    }
                            },
                            trailingIcon = {
                                Icon(
                                    painter = painterResource(id = categoryIcon),
                                    contentDescription = null
                                )
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

            if (state.isDownloadingDocument) {
                DownloadProgressDialog()
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

@Composable
fun DownloadProgressDialog() {

    Dialog(onDismissRequest = {}) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(16.dp)
            ) {
                DownloadDocumentAnimation()
                Text(text = "Descargando documento...")
            }
        }
    }

}

@Composable
fun DownloadDocumentAnimation(
    modifier: Modifier = Modifier
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.download_animation))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever
    )

    LottieAnimation(
        composition = composition,
        progress = { progress },
        modifier = modifier
    )
}

@Preview
@Composable
fun DownloadProgressDialogPreview() {
    CosasDeUnicorAppTheme {
        DownloadProgressDialog()
    }
}