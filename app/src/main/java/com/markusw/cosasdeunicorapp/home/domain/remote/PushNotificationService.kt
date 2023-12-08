package com.markusw.cosasdeunicorapp.home.domain.remote

import com.markusw.cosasdeunicorapp.home.domain.model.Message
import com.markusw.cosasdeunicorapp.core.utils.Result

interface PushNotificationService {
    suspend fun postNotification(message: Message): Result<Unit>
}