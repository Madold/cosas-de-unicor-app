package com.markusw.cosasdeunicorapp.tabulator.domain

import com.markusw.cosasdeunicorapp.tabulator.domain.model.AcademicProgram
import com.markusw.cosasdeunicorapp.tabulator.domain.model.IcfesResult

/**
 * Interface for the calculation of the weighted score of an ICFES result
 */
interface WeightingCalculation {
    fun calculateWeighted(icfesResults: IcfesResult, academicProgram: AcademicProgram): Float
}