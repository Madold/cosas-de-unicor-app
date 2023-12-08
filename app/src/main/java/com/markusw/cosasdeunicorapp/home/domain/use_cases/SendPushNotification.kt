package com.markusw.cosasdeunicorapp.home.domain.use_cases

import com.markusw.cosasdeunicorapp.home.domain.model.Message
import com.markusw.cosasdeunicorapp.home.domain.remote.PushNotificationService
import javax.inject.Inject

class SendPushNotification @Inject constructor(
    private val pushNotificationService: PushNotificationService
) {
    suspend operator fun invoke(message: Message) {
        pushNotificationService.postNotification(message)
    }
}