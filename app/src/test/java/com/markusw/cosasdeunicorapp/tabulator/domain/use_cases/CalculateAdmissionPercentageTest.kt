package com.markusw.cosasdeunicorapp.tabulator.domain.use_cases

import org.junit.Assert.*
import org.junit.Test

class CalculateAdmissionPercentageTest {

    private val calculateAdmissionPercentage: CalculateAdmissionPercentage = CalculateAdmissionPercentage()

    @Test
    fun `when the result of dividing the weighted by the maximum score is more than 1 then returns 99f`() {

        val weighted = 10f
        val maximumScore = 9f

        val result = calculateAdmissionPercentage(weighted, maximumScore)

        assertEquals(99f, result)
    }

    @Test
    fun `when the result of dividing the weighted by the maximum score is less than 1 then returns the result multiplied by 100`() {

        val weighted = 9f
        val maximumScore = 10f
        val result = calculateAdmissionPercentage(weighted, maximumScore)
        val expectedResult = weighted / maximumScore * 100


        assertEquals(result, expectedResult)
    }

}