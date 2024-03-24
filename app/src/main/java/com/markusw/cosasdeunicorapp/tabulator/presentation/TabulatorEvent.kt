package com.markusw.cosasdeunicorapp.tabulator.presentation

import com.markusw.cosasdeunicorapp.tabulator.domain.model.AcademicProgram

sealed interface TabulatorEvent {
    data class ChangeChemistryScore(val score: Float) : TabulatorEvent
    data class ChangePhysicsScore(val score: Float) : TabulatorEvent
    data class ChangeBiologyScore(val score: Float) : TabulatorEvent
    data class ChangeSpanishScore(val score: Float) : TabulatorEvent
    data class ChangePhilosophyScore(val score: Float) : TabulatorEvent
    data class ChangeMathScore(val score: Float) : TabulatorEvent
    data class ChangeHistoryScore(val score: Float) : TabulatorEvent
    data class ChangeGeographyScore(val score: Float) : TabulatorEvent
    data class ChangeEnglishScore(val score: Float) : TabulatorEvent
    data class ChangeSocialSciencesScore(val score: Float) : TabulatorEvent
    data class ChangeCriticalReadingScore(val score: Float) : TabulatorEvent
    data class ChangeNaturalSciencesScore(val score: Float) : TabulatorEvent
    data class ChangeSelectedTestType(val testType: TestType) : TabulatorEvent
    data object EvaluateScores : TabulatorEvent
    data class ChangeSelectedAcademicProgram(val academicProgram: AcademicProgram) : TabulatorEvent
}