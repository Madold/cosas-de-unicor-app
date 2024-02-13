package com.markusw.cosasdeunicorapp.tabulator.domain

/**
 * Represents the weights for the ICFES results before 2014
 */
data class WeightsBefore2014(
    val chemistryWeight: Float = 0f,
    val physicsWeight: Float = 0f,
    val biologyWeight: Float = 0f,
    val spanishWeight: Float = 0f,
    val philosophyWeight: Float = 0f,
    val mathWeight: Float = 0f,
    val socialSciencesWeight: Float = 0f,
    val englishWeight: Float = 0f,
)
