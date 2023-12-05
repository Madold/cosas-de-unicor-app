package com.markusw.cosasdeunicorapp.home.data.remote

import com.markusw.cosasdeunicorapp.core.presentation.UiText
import com.markusw.cosasdeunicorapp.core.utils.Result
import com.markusw.cosasdeunicorapp.home.domain.model.PushNotification
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Markus on 3-12-2023.
 * Service that handles the sending of push notifications
 * @param api the api to send the notifications to
 * @see PushNotificationApi
 */
class PushNotificationService @Inject constructor(
    private val api: PushNotificationApi
) {

    /**
     * Posts a notification to the server to be sent to a device
     * @param notification the notification to be sent
     * @return a Result indicating the success of the operation
     */
    suspend fun postNotification(notification: PushNotification): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                Timber.d("Sending notification: ${notification.data}")
                val response = api.postNotification(notification)

                if (!response.isSuccessful) {
                    Result.Error<Unit>(UiText.DynamicString(response.errorBody()?.string() ?: "null error"))
                }

                Result.Success(Unit)
            } catch (e: Exception) {
                Timber.e(e)
                Result.Error(UiText.DynamicString("Error: ${e.message}"))
            }
        }

    }

}