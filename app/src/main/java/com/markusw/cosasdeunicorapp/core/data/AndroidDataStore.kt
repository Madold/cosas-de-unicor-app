package com.markusw.cosasdeunicorapp.core.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.markusw.cosasdeunicorapp.core.domain.LocalDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AndroidDataStore (
    private val context: Context
): LocalDataStore {

    private val Context.dataStore by preferencesDataStore(name = "settings")
    override suspend fun save(key: String, value: String) {
        val dataStoreKey = stringPreferencesKey(key)
        context.dataStore.edit { settings ->
            settings[dataStoreKey] = value
        }
    }

    override suspend fun get(key: String): Flow<String?> {
        val dataStoreKey = stringPreferencesKey(key)
        val dataFlow: Flow<String?> = context
            .dataStore
            .data
            .map { preferences ->
                preferences[dataStoreKey]
            }
        return dataFlow
    }

}