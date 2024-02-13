package com.markusw.cosasdeunicorapp.tabulator.domain

/**
 * Class for the calculation of the weighted score of an ICFES result before 2005
 */
class Before2005: WeightingCalculation {
    override fun calculateWeighted(
        icfesResults: IcfesResult,
        academicProgram: AcademicProgram
    ): Float {
        //TODO: Implement weighted calculation for ICFES results before 2005
        return 0f
    }
}