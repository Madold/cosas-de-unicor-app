package com.markusw.cosasdeunicorapp.data

import com.markusw.cosasdeunicorapp.core.utils.Resource
import com.markusw.cosasdeunicorapp.data.model.Message
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    suspend fun getGlobalChatList(): Flow<List<Message>>
    suspend fun sendMessageToGlobalChat(message: Message): Resource<Unit>
}