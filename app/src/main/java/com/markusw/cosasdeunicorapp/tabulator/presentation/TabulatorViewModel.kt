package com.markusw.cosasdeunicorapp.tabulator.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.markusw.cosasdeunicorapp.tabulator.domain.repository.TabulatorRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class TabulatorViewModel @Inject constructor(
    tabulatorRepository: TabulatorRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TabulatorState())
    private val _academicProgramsList = tabulatorRepository.getAcademicPrograms()
    val uiState = combine(_uiState, _academicProgramsList) { state, academicPrograms ->
        state.copy(academicPrograms = academicPrograms)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
        TabulatorState()
    )

    /**
     * This function is called when the user interacts with the UI and triggers an event.
     * It updates the UI state based on the event.
     * @param event The event that was triggered by the user.
     */
    fun onEvent(event: TabulatorEvent) {
        when (event) {
            is TabulatorEvent.ChangeBiologyScore -> {
                _uiState.update {
                    it.copy(biologyScore = event.score)
                }
            }
            is TabulatorEvent.ChangeChemistryScore -> {
                _uiState.update {
                    it.copy(chemistryScore = event.score)
                }
            }
            is TabulatorEvent.ChangeCriticalReadingScore -> {
                _uiState.update {
                    it.copy(criticalReadingScore = event.score)
                }
            }
            is TabulatorEvent.ChangeEnglishScore -> {
                _uiState.update {
                    it.copy(englishScore = event.score)
                }
            }
            is TabulatorEvent.ChangeGeographyScore -> {
                _uiState.update {
                    it.copy(geographyScore = event.score)
                }
            }
            is TabulatorEvent.ChangeHistoryScore -> {
                _uiState.update {
                    it.copy(historyScore = event.score)
                }
            }
            is TabulatorEvent.ChangeMathScore -> {
                _uiState.update {
                    it.copy(mathScore = event.score)
                }
            }
            is TabulatorEvent.ChangeNaturalSciencesScore -> {
                _uiState.update {
                    it.copy(naturalSciencesScore = event.score)
                }
            }
            is TabulatorEvent.ChangePhilosophyScore -> {
                _uiState.update {
                    it.copy(philosophyScore = event.score)
                }
            }
            is TabulatorEvent.ChangePhysicsScore -> {
                _uiState.update {
                    it.copy(physicsScore = event.score)
                }
            }
            is TabulatorEvent.ChangeSelectedTestType -> {
                _uiState.update {
                    it.copy(selectedTestType = event.testType)
                }

            }
            is TabulatorEvent.ChangeSocialSciencesScore -> {
                _uiState.update {
                    it.copy(socialSciencesScore = event.score)
                }
            }
            is TabulatorEvent.ChangeSpanishScore -> {
                _uiState.update {
                    it.copy(spanishScore = event.score)
                }
            }

            is TabulatorEvent.ChangeSelectedAcademicProgram -> {
                _uiState.update {
                    it.copy(selectedAcademicProgram = event.academicProgram)
                }
            }

            is TabulatorEvent.EvaluateScores -> {
                // TODO Evaluate scores and provide a list of favorable programs to get admitted
            }


        }
    }

}