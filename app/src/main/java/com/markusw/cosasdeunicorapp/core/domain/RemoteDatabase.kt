package com.markusw.cosasdeunicorapp.core.domain

import com.markusw.cosasdeunicorapp.core.utils.Result
import com.markusw.cosasdeunicorapp.home.domain.model.Message
import com.markusw.cosasdeunicorapp.core.domain.model.User
import kotlinx.coroutines.flow.Flow

interface RemoteDatabase {
    suspend fun loadPreviousMessages(): List<Message>
    suspend fun sendMessageToGlobalChat(message: Message): Result<Unit>
    suspend fun saveUserInDatabase(user: User): Result<Unit>
    suspend fun onNewMessage(): Flow<Message>
}