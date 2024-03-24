package com.markusw.cosasdeunicorapp.tabulator.presentation

import com.markusw.cosasdeunicorapp.tabulator.domain.model.AcademicProgram
import com.markusw.cosasdeunicorapp.tabulator.domain.model.AdmissionResult

data class TabulatorState(
    val chemistryScore: Float = 0f,
    val physicsScore: Float = 0f,
    val biologyScore: Float = 0f,
    val spanishScore: Float = 0f,
    val philosophyScore: Float = 0f,
    val mathScore: Float = 0f,
    val historyScore: Float = 0f,
    val geographyScore: Float = 0f,
    val englishScore: Float = 0f,
    val socialSciencesScore: Float = 0f,
    val criticalReadingScore: Float = 0f,
    val naturalSciencesScore: Float = 0f,
    val selectedTestType: TestType = TestType.Before2005,
    val academicPrograms: List<AcademicProgram> = emptyList(),
    val admissionResults: List<AdmissionResult> = emptyList(),
    val selectedAcademicProgram: AcademicProgram? = null,
    val academicProgramName: String = "",
    val isCalculatingResults: Boolean = false
)
