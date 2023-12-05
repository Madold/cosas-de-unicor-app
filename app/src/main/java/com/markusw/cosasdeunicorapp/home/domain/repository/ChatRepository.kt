package com.markusw.cosasdeunicorapp.home.domain.repository

import com.markusw.cosasdeunicorapp.core.utils.Result
import com.markusw.cosasdeunicorapp.home.domain.model.Message
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    /**
     * Loads the previous messages from the database
     * @return a list of messages
     */
    suspend fun loadPreviousMessages(): List<Message>
    /**
     * Sends a message to the global chat
     * @param message the message to send
     * @return a Result object
     */
    suspend fun sendMessageToGlobalChat(message: Message): Result<Unit>
    /**
     * Returns a flow of messages that are sent to the global chat
     * @return a flow of messages
     * @see Message
     * @see Flow
     */
    suspend fun onNewMessage(): Flow<Message>
}