package com.markusw.cosasdeunicorapp.home.domain.repository

import kotlinx.coroutines.flow.Flow

interface PaginationSource<T, U> {
    suspend fun loadPage(): List<U>
}

