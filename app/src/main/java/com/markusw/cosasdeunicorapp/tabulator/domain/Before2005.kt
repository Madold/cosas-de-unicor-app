package com.markusw.cosasdeunicorapp.tabulator.domain

/**
 * Class for the calculation of the weighted score of an ICFES result before 2005
 */
class Before2005 : WeightingCalculation {
    override fun calculateWeighted(
        icfesResults: IcfesResult,
        academicProgram: AcademicProgram
    ): Float {

        val (
            chemistryWeight,
            physicsWeight,
            biologyWeight,
            spanishWeight,
            philosophyWeight,
            mathWeight,
            historyWeight,
            geographyWeight,
            englishWeight
        ) = academicProgram.weightsBefore2005

        val chemistryScore = icfesResults.chemistryScore * chemistryWeight
        val physicsScore = icfesResults.physicsScore * physicsWeight
        val biologyScore = icfesResults.biologyScore * biologyWeight
        val spanishScore = icfesResults.spanishScore * spanishWeight
        val philosophyScore = icfesResults.philosophyScore * philosophyWeight
        val mathScore = icfesResults.mathScore * mathWeight
        val historyScore = icfesResults.historyScore * historyWeight
        val geographyScore = icfesResults.geographyScore * geographyWeight
        val englishScore = icfesResults.englishScore * englishWeight
        val totalScore = chemistryScore + physicsScore + biologyScore + spanishScore + philosophyScore + mathScore + historyScore + geographyScore + englishScore

        return (totalScore / 100) * 9
    }
}