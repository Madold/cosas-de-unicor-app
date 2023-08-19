package com.markusw.cosasdeunicorapp.data

import com.markusw.cosasdeunicorapp.core.utils.Resource
import com.markusw.cosasdeunicorapp.data.model.Message
import com.markusw.cosasdeunicorapp.domain.services.RemoteDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AndroidChatRepository constructor(
    private val remoteDatabase: RemoteDatabase
) : ChatRepository {
    override suspend fun getGlobalChatList(): Flow<List<Message>> = flow {
         remoteDatabase.getGlobalChatList().collect {
             when (it) {
                 is Resource.Success -> {
                     it.data?.let { messages ->
                         emit(messages)
                     }
                 }
                 is Resource.Error -> {
                     emit(emptyList())
                 }
             }
         }
    }

    override suspend fun sendMessageToGlobalChat(message: Message): Resource<Unit> {
        return remoteDatabase.sendMessageToGlobalChat(message)
    }

}