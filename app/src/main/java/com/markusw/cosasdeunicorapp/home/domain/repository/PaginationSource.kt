package com.markusw.cosasdeunicorapp.home.domain.repository


interface PaginationSource<T, U> {
    suspend fun loadPage(): List<U>
}

