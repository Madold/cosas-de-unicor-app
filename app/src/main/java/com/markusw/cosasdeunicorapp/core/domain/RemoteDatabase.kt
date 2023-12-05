package com.markusw.cosasdeunicorapp.core.domain

import com.markusw.cosasdeunicorapp.core.utils.Result
import com.markusw.cosasdeunicorapp.home.domain.model.Message
import com.markusw.cosasdeunicorapp.core.domain.model.User
import kotlinx.coroutines.flow.Flow

interface RemoteDatabase {
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
     * Saves the user in the database
     * @param user the user to save
     * @return a Result object
     * @see User
     * @see Result
     */
    suspend fun saveUserInDatabase(user: User): Result<Unit>

    /**
     * Returns a flow of messages that are sent to the global chat
     * @return a flow of messages
     * @see Message
     * @see Flow
     */
    suspend fun onNewMessage(): Flow<Message>
}