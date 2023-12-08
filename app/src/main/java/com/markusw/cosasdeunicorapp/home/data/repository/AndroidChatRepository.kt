package com.markusw.cosasdeunicorapp.home.data.repository

import com.markusw.cosasdeunicorapp.core.domain.RemoteDatabase
import com.markusw.cosasdeunicorapp.core.utils.Result
import com.markusw.cosasdeunicorapp.home.domain.model.Message
import com.markusw.cosasdeunicorapp.home.domain.repository.ChatRepository

// Created by Markus on 5-12-2023.

/**
 * Android implementation of the [ChatRepository] interface
 * @param remoteDatabase the remote database implementation
 * @see ChatRepository
 */
class AndroidChatRepository(
    private val remoteDatabase: RemoteDatabase,
) : ChatRepository {
    override suspend fun loadPreviousMessages() = remoteDatabase.loadPreviousMessages()
    override suspend fun sendMessageToGlobalChat(message: Message): Result<Unit> {
        return remoteDatabase.sendMessageToGlobalChat(message)
    }

    override suspend fun onNewMessage() = remoteDatabase.onNewMessage()

}