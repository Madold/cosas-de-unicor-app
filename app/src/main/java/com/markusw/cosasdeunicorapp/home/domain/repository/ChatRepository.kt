package com.markusw.cosasdeunicorapp.home.domain.repository

import com.markusw.cosasdeunicorapp.core.utils.Result
import com.markusw.cosasdeunicorapp.home.domain.model.Message
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    suspend fun loadPreviousMessages(): List<Message>
    suspend fun sendMessageToGlobalChat(message: Message): Result<Unit>
    suspend fun onNewMessage(): Flow<Message>
}