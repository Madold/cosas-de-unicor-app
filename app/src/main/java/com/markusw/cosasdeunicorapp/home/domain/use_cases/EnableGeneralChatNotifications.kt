package com.markusw.cosasdeunicorapp.home.domain.use_cases

import com.markusw.cosasdeunicorapp.home.domain.remote.PushNotificationService
import javax.inject.Inject

class EnableGeneralChatNotifications @Inject constructor(
    private val pushNotificationService: PushNotificationService
) {

    operator fun invoke() {
        pushNotificationService.enableGeneralChatNotifications()
    }

}