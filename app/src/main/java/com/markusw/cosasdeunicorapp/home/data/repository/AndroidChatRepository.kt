package com.markusw.cosasdeunicorapp.home.data.repository

import com.markusw.cosasdeunicorapp.core.domain.RemoteDatabase
import com.markusw.cosasdeunicorapp.home.domain.model.Message
import com.markusw.cosasdeunicorapp.home.domain.repository.ChatRepository


class AndroidChatRepository constructor(
    private val remoteDatabase: RemoteDatabase
) : ChatRepository {
    override suspend fun loadPreviousMessages() = remoteDatabase.loadPreviousMessages()
    override suspend fun sendMessageToGlobalChat(message: Message) = remoteDatabase.sendMessageToGlobalChat(message)
    override suspend fun onNewMessage() = remoteDatabase.onNewMessage()

}