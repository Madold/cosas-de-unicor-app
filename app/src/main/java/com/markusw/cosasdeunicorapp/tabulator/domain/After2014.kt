package com.markusw.cosasdeunicorapp.tabulator.domain

import com.markusw.cosasdeunicorapp.tabulator.domain.model.AcademicProgram
import com.markusw.cosasdeunicorapp.tabulator.domain.model.IcfesResult

/**
 * Encapsulates the calculation logic for the weighted score of an ICFES result after 2014
 */
class After2014 : WeightingCalculation {
    override fun calculateWeighted(
        icfesResults: IcfesResult,
        academicProgram: AcademicProgram
    ): Float {

        val (
            criticalReadingWeight,
            naturalSciencesWeight,
            socialSciencesWeight,
            mathWeight,
            englishWeight
        ) = academicProgram.weightsAfter2014

        val criticalReadingScore = icfesResults.criticalReadingScore * criticalReadingWeight
        val mathScore = icfesResults.mathScore * mathWeight
        val socialSciencesScore = icfesResults.socialSciencesScore * socialSciencesWeight
        val naturalSciencesScore = icfesResults.naturalSciencesScore * naturalSciencesWeight
        val englishScore = icfesResults.englishScore * englishWeight
        val totalScore = criticalReadingScore + mathScore + socialSciencesScore + naturalSciencesScore + englishScore

        return (totalScore / 100) * 5
    }

}