package com.markusw.cosasdeunicorapp.home.domain.repository

interface NewsRepository {
    suspend fun loadPreviousNews()
}