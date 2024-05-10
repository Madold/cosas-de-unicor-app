@file:OptIn(ExperimentalFoundationApi::class)

package com.markusw.cosasdeunicorapp.tabulator.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.markusw.cosasdeunicorapp.core.ext.pop
import com.markusw.cosasdeunicorapp.core.presentation.AppTopBar
import com.markusw.cosasdeunicorapp.core.presentation.Button
import com.markusw.cosasdeunicorapp.core.presentation.ConfirmDialog
import com.markusw.cosasdeunicorapp.tabulator.presentation.composables.After2014ResultsPreviewTable
import com.markusw.cosasdeunicorapp.tabulator.presentation.composables.Before2005ResultsPreviewTable
import com.markusw.cosasdeunicorapp.tabulator.presentation.composables.Before2014ResultsPreviewTable
import com.markusw.cosasdeunicorapp.tabulator.presentation.views.AcademicProgramSelectionView
import com.markusw.cosasdeunicorapp.tabulator.presentation.views.TabulatorResultsView
import com.markusw.cosasdeunicorapp.tabulator.presentation.views.TestScoresView
import com.markusw.cosasdeunicorapp.tabulator.presentation.views.TestTypeSelectionView
import com.markusw.cosasdeunicorapp.ui.theme.CosasDeUnicorAppTheme
import kotlinx.coroutines.launch

const val LAST_PAGE_INDEX = 3
const val FIRST_PAGE_INDEX = 0
const val SECOND_PAGE_INDEX = 1
const val THIRD_PAGE_INDEX = 2

@Composable
fun TabulatorScreen(
    mainNavController: NavController,
    state: TabulatorState,
    onEvent: (TabulatorEvent) -> Unit = { }
) {

    var isConfirmDialogVisible by rememberSaveable { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState(
        pageCount = { 4 }
    )
    val currentScreenIndex by remember {
        derivedStateOf { pagerState.currentPage }
    }
    val nextButtonText = remember(currentScreenIndex) {
        when (currentScreenIndex) {
            THIRD_PAGE_INDEX -> "Calcular"
            LAST_PAGE_INDEX -> "Finalizar"
            else -> "Siguiente"
        }
    }

    fun navigateToPreviousPage() {
        if (currentScreenIndex != FIRST_PAGE_INDEX) {
            coroutineScope.launch {
                pagerState.animateScrollToPage(
                    currentScreenIndex - 1
                )
            }
        }
    }

    fun navigateToNextPage() {

        if (currentScreenIndex == LAST_PAGE_INDEX) {
            mainNavController.pop()
            return
        }

        if (currentScreenIndex == THIRD_PAGE_INDEX) {
            isConfirmDialogVisible = true
            return
        }

        if (currentScreenIndex != pagerState.pageCount - 1) {
            coroutineScope.launch {
                pagerState.animateScrollToPage(
                    currentScreenIndex + 1
                )
            }
        }
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = {
                    Text(
                        "Tabulador",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { mainNavController.pop() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                }
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = ::navigateToPreviousPage,
                    enabled = currentScreenIndex != 0
                ) {
                    Text("Anterior")
                }

                Button(
                    onClick = ::navigateToNextPage,
                    enabled = state.selectedAcademicProgram != null || currentScreenIndex != SECOND_PAGE_INDEX
                ) {
                    Text(nextButtonText)
                }

            }
        }
    ) { padding ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .padding(padding)
                .padding(16.dp),
            userScrollEnabled = false
        ) { pageIndex ->
            when (pageIndex) {
                0 -> {
                    TestTypeSelectionView(
                        state = state,
                        onEvent = onEvent
                    )
                }

                1 -> {
                    AcademicProgramSelectionView(
                        state = state,
                        onEvent = onEvent
                    )
                }

                2 -> {
                    TestScoresView(
                        state = state,
                        onEvent = onEvent
                    )
                }

                3 -> {
                    TabulatorResultsView(
                        state = state,
                        onEvent = onEvent
                    )
                }
            }
        }

        if (isConfirmDialogVisible) {
            ConfirmDialog(
                title = {
                    Text(
                        text = "¿Estás seguro de que deseas finalizar?",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                body = {
                    Text(
                        text = "Estas a punto de finalizar el proceso de tabulación. Revisa tus datos antes de continuar:",
                        textAlign = TextAlign.Justify
                    )

                    Text(
                        text = buildAnnotatedString {
                            append("Programa académico: ")
                            withStyle(
                                style = SpanStyle(
                                    fontWeight = FontWeight.Bold
                                )
                            ) {
                                append(state.selectedAcademicProgram?.name ?: "NN")
                            }

                        },
                    )

                    Text(
                        text = buildAnnotatedString {
                            append("Tipo de prueba: ")
                            withStyle(
                                style = SpanStyle(
                                    fontWeight = FontWeight.Bold
                                )
                            ) {
                                append(state.selectedTestType.label)
                            }
                        },
                    )

                    Text(
                        text = "Tus puntajes:",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    when (state.selectedTestType) {
                        TestType.After2005 -> {
                            Before2014ResultsPreviewTable(
                                state = state,
                                firstColumnWeight = 0.5f,
                                secondColumnWeight = 0.5f
                            )
                        }

                        TestType.After2014 -> {
                            After2014ResultsPreviewTable(
                                state = state,
                                firstColumnWeight = 0.5f,
                                secondColumnWeight = 0.5f
                            )
                        }

                        TestType.Before2005 -> {
                            Before2005ResultsPreviewTable(
                                state = state,
                                firstColumnWeight = 0.5f,
                                secondColumnWeight = 0.5f
                            )
                        }
                    }


                },
                onConfirm = {
                    isConfirmDialogVisible = false
                    onEvent(TabulatorEvent.EvaluateScores)
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(LAST_PAGE_INDEX)
                    }
                },
                onDismiss = {
                    isConfirmDialogVisible = false
                }
            )
        }
    }
}

@Preview(
    showBackground = true
)
@Composable
fun TabulatorScreenPreview() {

    CosasDeUnicorAppTheme(
        darkTheme = true
    ) {
        TabulatorScreen(
            mainNavController = rememberNavController(),
            state = TabulatorState()
        )
    }
}