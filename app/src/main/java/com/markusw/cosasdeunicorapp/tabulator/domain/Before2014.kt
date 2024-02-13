package com.markusw.cosasdeunicorapp.tabulator.domain

class Before2014: WeightingCalculation {
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
        socialSciencesWeight,
        englishWeight,
        ) = academicProgram.weightsBefore2014

        val chemistryScore = icfesResults.chemistryScore * chemistryWeight
        val physicsScore = icfesResults.physicsScore * physicsWeight
        val biologyScore = icfesResults.biologyScore * biologyWeight
        val spanishScore = icfesResults.spanishScore * spanishWeight
        val philosophyScore = icfesResults.philosophyScore * philosophyWeight
        val mathScore = icfesResults.mathScore * mathWeight
        val socialSciencesScore = icfesResults.socialSciencesScore * socialSciencesWeight
        val englishScore = icfesResults.englishScore * englishWeight
        val totalScore = chemistryScore + physicsScore + biologyScore + spanishScore + philosophyScore + mathScore + socialSciencesScore + englishScore

        return (totalScore / 100) * 8
    }
}