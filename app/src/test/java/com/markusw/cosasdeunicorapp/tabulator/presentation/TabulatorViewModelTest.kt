package com.markusw.cosasdeunicorapp.tabulator.presentation

import app.cash.turbine.test
import com.markusw.cosasdeunicorapp.tabulator.data.FakeTabulatorRepository
import com.markusw.cosasdeunicorapp.tabulator.domain.repository.TabulatorRepository
import com.markusw.cosasdeunicorapp.tabulator.domain.use_cases.CalculateAdmissionPercentage
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test


class TabulatorViewModelTest {

    private lateinit var tabulatorRepository: TabulatorRepository
    private lateinit var calculateAdmissionPercentage: CalculateAdmissionPercentage
    private lateinit var tabulatorViewModel: TabulatorViewModel

    @Before
    fun setUp() {
        tabulatorRepository = FakeTabulatorRepository()
        calculateAdmissionPercentage = CalculateAdmissionPercentage()
        tabulatorViewModel = TabulatorViewModel(tabulatorRepository, calculateAdmissionPercentage)
    }

    @Test
    fun `should display the correct initial state`() {
        // Given
        val expectedState = TabulatorState()

        // When
        val actualState = tabulatorViewModel.uiState.value

        // Then
        assertEquals(expectedState, actualState)
    }


    /*
    @Test
    fun `should update biology score`()  = runTest {
        tabulatorViewModel.uiState.test {
            var emission = awaitItem()
            assertEquals(0, emission.biologyScore)
            tabulatorViewModel.onEvent(TabulatorEvent.ChangeBiologyScore(50))
            emission = awaitItem()
            assertEquals(50, emission.biologyScore)
        }
    }*/

}