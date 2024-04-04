package com.markusw.cosasdeunicorapp.tabulator.data

import com.markusw.cosasdeunicorapp.tabulator.domain.model.AcademicProgram
import com.markusw.cosasdeunicorapp.tabulator.domain.repository.TabulatorRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeTabulatorRepository : TabulatorRepository {
    override fun getAcademicPrograms(): Flow<List<AcademicProgram>> {
        return flow {
            emit(
                listOf(
                    AcademicProgram(
                        name = "Ingeniería de Sistemas",
                        minimumScore = 300f,
                        maximumScore = 500f
                    )
                )
            )
        }
    }
}