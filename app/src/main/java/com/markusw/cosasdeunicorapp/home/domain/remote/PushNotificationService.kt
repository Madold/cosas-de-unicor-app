package com.markusw.cosasdeunicorapp.home.domain.remote

import com.markusw.cosasdeunicorapp.home.domain.model.Message
import com.markusw.cosasdeunicorapp.core.utils.Result

interface PushNotificationService {
    /**
     * This method is used to send a push notification to the general chat
     */
    suspend fun postNotification(message: Message): Result<Unit>
    /**
     * This method is used to enable notifications for the general chat
     */
    fun enableGeneralChatNotifications()
    /**
     * This method is used to disable notifications for the general chat
     */
    fun disableGeneralChatNotifications()
}