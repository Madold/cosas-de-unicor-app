package com.markusw.cosasdeunicorapp.tabulator.domain.model

/**
 * Represents the weights for the ICFES results before 2005
 */
data class WeightsBefore2005(
    val chemistryWeight: Float = 0f,
    val physicsWeight: Float = 0f,
    val biologyWeight: Float = 0f,
    val spanishWeight: Float = 0f,
    val philosophyWeight: Float = 0f,
    val mathWeight: Float = 0f,
    val historyWeight: Float = 0f,
    val geographyWeight: Float = 0f,
    val englishWeight: Float = 0f,
)