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
        val result = weighted / maximumScore

        if (result >= 1f) {
            return 0.99f * 100
        }

        return result * 100
    }

}