package com.markusw.cosasdeunicorapp.tabulator.domain.repository

import com.markusw.cosasdeunicorapp.core.utils.Result
import com.markusw.cosasdeunicorapp.tabulator.domain.model.AcademicProgram
import kotlinx.coroutines.flow.Flow

interface TabulatorRepository {
    fun getAcademicPrograms(): Flow<List<AcademicProgram>>
}