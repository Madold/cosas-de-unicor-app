package com.markusw.cosasdeunicorapp.tabulator.domain.model

/**
 * Represents the result of an ICFES test in any of it's editions.
 */
data class IcfesResult(
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
)
