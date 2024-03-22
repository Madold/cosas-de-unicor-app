package com.markusw.cosasdeunicorapp.tabulator.data.repository

import com.markusw.cosasdeunicorapp.core.domain.RemoteDatabase
import com.markusw.cosasdeunicorapp.core.presentation.UiText
import com.markusw.cosasdeunicorapp.core.utils.Result
import com.markusw.cosasdeunicorapp.tabulator.domain.model.AcademicProgram
import com.markusw.cosasdeunicorapp.tabulator.domain.repository.TabulatorRepository
import kotlinx.coroutines.flow.Flow
import timber.log.Timber

class AndroidTabulatorRepository(
    private val remoteDatabase: RemoteDatabase
): TabulatorRepository {
    override fun getAcademicPrograms(): Flow<List<AcademicProgram>> {
        return remoteDatabase.getAcademicPrograms()
    }
}