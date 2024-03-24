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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.markusw.cosasdeunicorapp.core.ext.pop
import com.markusw.cosasdeunicorapp.core.presentation.AppTopBar
import com.markusw.cosasdeunicorapp.core.presentation.Button
import com.markusw.cosasdeunicorapp.tabulator.presentation.views.AcademicProgramSelectionView
import com.markusw.cosasdeunicorapp.tabulator.presentation.views.TabulatorResultsView
import com.markusw.cosasdeunicorapp.tabulator.presentation.views.TestScoresView
import com.markusw.cosasdeunicorapp.tabulator.presentation.views.TestTypeSelectionView
import com.markusw.cosasdeunicorapp.ui.theme.CosasDeUnicorAppTheme
import kotlinx.coroutines.launch

const val LAST_PAGE_INDEX  = 3
const val FIRST_PAGE_INDEX = 0
const val SECOND_PAGE_INDEX = 1
const val THIRD_PAGE_INDEX = 2

@Composable
fun TabulatorScreen(
    mainNavController: NavController,
    state: TabulatorState,
    onEvent: (TabulatorEvent) -> Unit = { }
) {

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
                    Text("Tabulador")
                },
                navigationIcon = {
                    IconButton(onClick = { mainNavController.pop() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = null
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
        },
        content = { padding ->
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
                ,
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
        }
    )
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