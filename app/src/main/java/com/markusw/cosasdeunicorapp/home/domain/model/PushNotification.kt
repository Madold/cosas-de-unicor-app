package com.markusw.cosasdeunicorapp.home.domain.model

/**
 * Created by Markus on 3-12-2023.
 * Represents a push notification to be sent to a device
 * @param data the data of the notification
 * @param to the token of the device to send the notification to
 * @see NotificationData
 */
data class PushNotification(
    val data: NotificationData,
    val to: String
)

/**
 * Represents the data of a push notification
 * @param title the title of the notification
 * @param message the message of the notification
 */
data class NotificationData(
    val title: String,
    val message: String
)
