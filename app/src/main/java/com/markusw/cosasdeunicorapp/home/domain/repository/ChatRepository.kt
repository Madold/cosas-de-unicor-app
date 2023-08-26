package com.markusw.cosasdeunicorapp.home.domain.repository

import com.markusw.cosasdeunicorapp.core.utils.Resource
import com.markusw.cosasdeunicorapp.home.domain.model.Message
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    suspend fun getGlobalChatList(): Flow<Resource<List<Message>>>
    suspend fun sendMessageToGlobalChat(message: Message): Resource<Unit>
}