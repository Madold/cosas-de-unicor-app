package com.markusw.cosasdeunicorapp.tabulator.domain

/**
 * Encapsulates the calculation logic for the weighted score of an ICFES result after 2014
 */
class After2014: WeightingCalculation {
    override fun calculateWeighted(
        icfesResults: IcfesResult,
        academicProgram: AcademicProgram
    ): Float {
        TODO("Implement weighted calculation for ICFES results after 2014")
    }

}