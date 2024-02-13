package com.markusw.cosasdeunicorapp.tabulator.domain

/**
 * Interface for the calculation of the weighted score of an ICFES result
 */
interface WeightingCalculation {
    fun calculateWeighted(icfesResults: IcfesResult, academicProgram: AcademicProgram): Float
}