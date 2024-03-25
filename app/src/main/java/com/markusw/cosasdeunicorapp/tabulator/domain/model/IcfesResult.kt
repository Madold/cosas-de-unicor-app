package com.markusw.cosasdeunicorapp.tabulator.domain.model

/**
 * Represents the result of an ICFES test in any of it's editions.
 */
data class IcfesResult(
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
)
