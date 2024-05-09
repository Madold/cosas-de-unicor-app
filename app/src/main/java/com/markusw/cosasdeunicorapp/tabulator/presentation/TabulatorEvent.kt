package com.markusw.cosasdeunicorapp.tabulator.presentation

import com.markusw.cosasdeunicorapp.tabulator.domain.model.AcademicProgram

sealed interface TabulatorEvent {
    data class ChangeChemistryScore(val score: Int) : TabulatorEvent
    data class ChangePhysicsScore(val score: Int) : TabulatorEvent
    data class ChangeBiologyScore(val score: Int) : TabulatorEvent
    data class ChangeSpanishScore(val score: Int) : TabulatorEvent
    data class ChangePhilosophyScore(val score: Int) : TabulatorEvent
    data class ChangeMathScore(val score: Int) : TabulatorEvent
    data class ChangeHistoryScore(val score: Int) : TabulatorEvent
    data class ChangeGeographyScore(val score: Int) : TabulatorEvent
    data class ChangeEnglishScore(val score: Int) : TabulatorEvent
    data class ChangeSocialSciencesScore(val score: Int) : TabulatorEvent
    data class ChangeCriticalReadingScore(val score: Int) : TabulatorEvent
    data class ChangeNaturalSciencesScore(val score: Int) : TabulatorEvent
    data class ChangeSelectedTestType(val testType: TestType) : TabulatorEvent
    data object EvaluateScores : TabulatorEvent
    data class ChangeSelectedAcademicProgram(val academicProgram: AcademicProgram) : TabulatorEvent
    data class ChangeAcademicProgramName(val name: String): TabulatorEvent
}