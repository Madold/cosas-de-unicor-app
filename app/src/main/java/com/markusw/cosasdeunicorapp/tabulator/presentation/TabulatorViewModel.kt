package com.markusw.cosasdeunicorapp.tabulator.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.markusw.cosasdeunicorapp.tabulator.domain.After2014
import com.markusw.cosasdeunicorapp.tabulator.domain.Before2005
import com.markusw.cosasdeunicorapp.tabulator.domain.Before2014
import com.markusw.cosasdeunicorapp.tabulator.domain.WeightingCalculation
import com.markusw.cosasdeunicorapp.tabulator.domain.model.AdmissionResult
import com.markusw.cosasdeunicorapp.tabulator.domain.model.IcfesResult
import com.markusw.cosasdeunicorapp.tabulator.domain.repository.TabulatorRepository
import com.markusw.cosasdeunicorapp.tabulator.domain.use_cases.CalculateAdmissionPercentage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class TabulatorViewModel @Inject constructor(
    tabulatorRepository: TabulatorRepository,
    private val calculateAdmissionPercentage: CalculateAdmissionPercentage
) : ViewModel() {

    private var weightingCalculation: WeightingCalculation = Before2005()
    private val _uiState = MutableStateFlow(TabulatorState())
    private val _academicProgramsList = combine(
        _uiState,
        tabulatorRepository.getAcademicPrograms()
    ) { uiState, academicPrograms ->
        when (uiState.academicProgramName) {
            "" -> academicPrograms
            else -> academicPrograms.filter {
                it.name.contains(
                    uiState.academicProgramName,
                    ignoreCase = true
                )
            }
        }
    }
    val uiState = combine(
        _uiState,
        _academicProgramsList
    ) { state, academicPrograms ->
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

                weightingCalculation = when (event.testType) {
                    is TestType.After2005 -> Before2014()
                    is TestType.After2014 -> After2014()
                    is TestType.Before2005 -> Before2005()
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

            is TabulatorEvent.ChangeAcademicProgramName -> {
                _uiState.update {
                    it.copy(
                        academicProgramName = event.name
                    )
                }
            }

            is TabulatorEvent.EvaluateScores -> {
                _uiState.update {
                    it.copy(isCalculatingResults = true)
                }

                viewModelScope.launch {
                    val icfesResult = IcfesResult(
                        chemistryScore = uiState.value.chemistryScore,
                        physicsScore = uiState.value.physicsScore,
                        biologyScore = uiState.value.biologyScore,
                        spanishScore = uiState.value.spanishScore,
                        philosophyScore = uiState.value.philosophyScore,
                        mathScore = uiState.value.mathScore,
                        historyScore = uiState.value.historyScore,
                        geographyScore = uiState.value.geographyScore,
                        englishScore = uiState.value.englishScore,
                        socialSciencesScore = uiState.value.socialSciencesScore,
                        criticalReadingScore = uiState.value.criticalReadingScore,
                        naturalSciencesScore = uiState.value.naturalSciencesScore
                    )
                    val admissionResults = mutableListOf<AdmissionResult>()

                    uiState.value.academicPrograms.forEach { academicProgram ->
                        val weighted = uiState.value.selectedAcademicProgram?.let {
                            weightingCalculation.calculateWeighted(
                                icfesResults = icfesResult,
                                academicProgram = it
                            )
                        } ?: 0f

                        val admissionPercentage =
                            calculateAdmissionPercentage(weighted, academicProgram.maximumScore)

                        admissionResults.add(
                            AdmissionResult(
                                academicProgram,
                                admissionPercentage,
                                weighted
                            )
                        )
                    }

                    admissionResults.sortByDescending { it.admissionPercentage }

                    _uiState.update {
                        it.copy(
                            admissionResults = admissionResults.toList(),
                            isCalculatingResults = false
                        )
                    }
                }
            }
        }
    }

}