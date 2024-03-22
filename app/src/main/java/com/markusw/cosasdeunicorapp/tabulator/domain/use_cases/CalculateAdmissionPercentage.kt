package com.markusw.cosasdeunicorapp.tabulator.domain.use_cases

import javax.inject.Inject

class CalculateAdmissionPercentage @Inject constructor() {
    /**
     * Calculates the admission percentage of a student for entering an Academic Program.
     * @param weighted The weighted score of the student.
     * @param maximumScore The maximum score of the Academic Program.
     * @return The admission percentage of the student.
     */
    operator fun invoke(weighted: Float, maximumScore: Float ): Float {
        return (weighted / maximumScore) * 100
    }

}