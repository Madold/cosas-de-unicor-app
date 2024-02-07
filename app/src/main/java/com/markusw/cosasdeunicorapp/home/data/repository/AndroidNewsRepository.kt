package com.markusw.cosasdeunicorapp.home.data.repository

import com.markusw.cosasdeunicorapp.core.domain.RemoteDatabase
import com.markusw.cosasdeunicorapp.home.domain.model.News
import com.markusw.cosasdeunicorapp.home.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow

class AndroidNewsRepository(
    private val remoteDatabase: RemoteDatabase
): NewsRepository {

    override suspend fun loadPreviousNews(): List<News> {
        return remoteDatabase.loadPreviousNews()
    }

    override suspend fun onNewNews(): Flow<News> {
        return remoteDatabase.onNewNews()
    }

}