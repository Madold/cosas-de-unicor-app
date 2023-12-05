package com.markusw.cosasdeunicorapp.home.data.repository

import com.markusw.cosasdeunicorapp.core.domain.RemoteDatabase
import com.markusw.cosasdeunicorapp.core.utils.Result
import com.markusw.cosasdeunicorapp.home.data.remote.PushNotificationService
import com.markusw.cosasdeunicorapp.home.domain.model.Message
import com.markusw.cosasdeunicorapp.home.domain.model.NotificationData
import com.markusw.cosasdeunicorapp.home.domain.model.PushNotification
import com.markusw.cosasdeunicorapp.home.domain.repository.ChatRepository
import timber.log.Timber


/**
 * Created by Markus on 3-12-2023.
 * Concrete implementation of the ChatRepository interface for android
 * @param remoteDatabase the remote database to get the data from
 * @param pushNotificationService the service to send push notifications
 * @see ChatRepository
 * @see RemoteDatabase
 * @see PushNotificationService
 */
class AndroidChatRepository(
    private val remoteDatabase: RemoteDatabase,
    private val pushNotificationService: PushNotificationService
) : ChatRepository {
    override suspend fun loadPreviousMessages() = remoteDatabase.loadPreviousMessages()
    override suspend fun sendMessageToGlobalChat(message: Message): Result<Unit> {
        message.content.replyTo?.also { sendNotification(message) }
        return remoteDatabase.sendMessageToGlobalChat(message)
    }

    /**
     * Sends a notification to the device of the user that sent the message that was replied to
     * @param message the message that was replied to
     */
    private suspend fun sendNotification(message: Message) {
        message.content.replyTo?.let {
            pushNotificationService.postNotification(
                PushNotification(
                    data = NotificationData(
                        title = "New reply from ${message.sender.displayName}",
                        message = message.content.text,
                    ),
                    to = "/topics/${message.content.replyTo.sender.uid}"
                )
            )
        }
    }

    override suspend fun onNewMessage() = remoteDatabase.onNewMessage()

}