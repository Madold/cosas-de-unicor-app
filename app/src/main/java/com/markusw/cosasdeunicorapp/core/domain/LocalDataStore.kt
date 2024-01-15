package com.markusw.cosasdeunicorapp.core.domain

import kotlinx.coroutines.flow.Flow

interface LocalDataStore {
    suspend fun save(key: String, value: String)
    suspend fun get(key: String): Flow<String?>
}