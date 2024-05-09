package com.markusw.cosasdeunicorapp.tabulator.domain.model

/**
 * Represents an academic program in the university.
 */
data class AcademicProgram(
    val name: String = "",
    val weightsBefore2005: WeightsBefore2005 = WeightsBefore2005(),
    val weightsBefore2014: WeightsBefore2014 = WeightsBefore2014(),
    val weightsAfter2014: WeightsAfter2014 = WeightsAfter2014(),
    val minimumScore: Float = 0f,
    val maximumScore: Float = 0f
)
