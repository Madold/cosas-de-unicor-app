package com.markusw.cosasdeunicorapp.tabulator.presentation

import com.markusw.cosasdeunicorapp.tabulator.domain.model.AcademicProgram
import com.markusw.cosasdeunicorapp.tabulator.domain.model.AdmissionResult

data class TabulatorState(
    val chemistryScore: Int = 0,
    val physicsScore: Int = 0,
    val biologyScore: Int = 0,
    val spanishScore: Int = 0,
    val philosophyScore: Int = 0,
    val mathScore: Int = 0,
    val historyScore: Int = 0,
    val geographyScore: Int = 0,
    val englishScore: Int = 0,
    val socialSciencesScore: Int = 0,
    val criticalReadingScore: Int = 0,
    val naturalSciencesScore: Int = 0,
    val selectedTestType: TestType = TestType.Before2005,
    val academicPrograms: List<AcademicProgram> = emptyList(),
    val admissionResults: List<AdmissionResult> = emptyList(),
    val selectedAcademicProgram: AcademicProgram? = null,
    val academicProgramName: String = "",
    val isCalculatingResults: Boolean = false
)
