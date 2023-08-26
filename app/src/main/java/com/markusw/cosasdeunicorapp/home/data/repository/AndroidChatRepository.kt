package com.markusw.cosasdeunicorapp.home.data.repository

import com.markusw.cosasdeunicorapp.core.utils.Resource
import com.markusw.cosasdeunicorapp.home.domain.model.Message
import com.markusw.cosasdeunicorapp.core.domain.RemoteDatabase
import com.markusw.cosasdeunicorapp.home.domain.repository.ChatRepository
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