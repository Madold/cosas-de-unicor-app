@file:OptIn(ExperimentalCoroutinesApi::class)

package com.markusw.cosasdeunicorapp.tabulator.presentation

import com.markusw.cosasdeunicorapp.tabulator.domain.model.AcademicProgram
import com.markusw.cosasdeunicorapp.tabulator.domain.repository.TabulatorRepository
import com.markusw.cosasdeunicorapp.tabulator.domain.use_cases.CalculateAdmissionPercentage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class TabulatorViewModelTest {

    private lateinit var fakeTabulatorRepository: TabulatorRepository
    private lateinit var calculateAdmissionPercentage: CalculateAdmissionPercentage
    private lateinit var viewModel: TabulatorViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        fakeTabulatorRepository = object : TabulatorRepository {
            override fun getAcademicPrograms(): Flow<List<AcademicProgram>> {
                return flow {
                    emit(emptyList())
                }
            }
        }
        calculateAdmissionPercentage = CalculateAdmissionPercentage()
        viewModel = TabulatorViewModel(fakeTabulatorRepository, calculateAdmissionPercentage)
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should have the correct initial state`() {
        val uiState = viewModel.uiState.value
        val expectedUiState = TabulatorState()
        assertEquals(uiState, expectedUiState)
    }

    /*@Test
    fun `should update the UI state when an event is triggered`() = runTest {
        val programName = "Software Engineering"
        viewModel.onEvent(TabulatorEvent.ChangeAcademicProgramName(programName))
        advanceUntilIdle()
        val job = CoroutineScope(testDispatcher).launch {
            viewModel.uiState.collectLatest {
                println(it)
                assertEquals(it.academicProgramName, programName)
            }
        }

        advanceUntilIdle()
        job.cancel()


    }*/

}