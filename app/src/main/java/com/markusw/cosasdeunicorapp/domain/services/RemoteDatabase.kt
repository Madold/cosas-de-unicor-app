package com.markusw.cosasdeunicorapp.domain.services

import com.markusw.cosasdeunicorapp.core.utils.Resource
import com.markusw.cosasdeunicorapp.data.model.Message
import com.markusw.cosasdeunicorapp.data.model.User
import kotlinx.coroutines.flow.Flow


interface RemoteDatabase {
    suspend fun getGlobalChatList(): Flow<Resource<List<Message>>>
    suspend fun sendMessageToGlobalChat(message: Message): Resource<Unit>
    suspend fun saveUserInDatabase(user: User): Resource<Unit>
}