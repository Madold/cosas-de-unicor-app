package com.markusw.cosasdeunicorapp.home.domain.repository

import com.markusw.cosasdeunicorapp.home.domain.model.News
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    suspend fun loadPreviousNews(): List<News>

    suspend fun onNewNews(): Flow<News>
}