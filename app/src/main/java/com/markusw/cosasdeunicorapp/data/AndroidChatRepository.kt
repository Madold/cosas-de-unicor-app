package com.markusw.cosasdeunicorapp.data

import com.markusw.cosasdeunicorapp.core.utils.Resource
import com.markusw.cosasdeunicorapp.data.model.Message
import com.markusw.cosasdeunicorapp.domain.services.RemoteDatabase
import kotlinx.coroutines.flow.Flow


class AndroidChatRepository constructor(
    private val remoteDatabase: RemoteDatabase
) : ChatRepository {
    override suspend fun getGlobalChatList(): Flow<Resource<List<Message>>> {
        return remoteDatabase.getGlobalChatList()
    }


    override suspend fun sendMessageToGlobalChat(message: Message): Resource<Unit> {
        return remoteDatabase.sendMessageToGlobalChat(message)
    }

}