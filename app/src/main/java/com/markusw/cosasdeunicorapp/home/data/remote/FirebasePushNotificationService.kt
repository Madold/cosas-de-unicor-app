package com.markusw.cosasdeunicorapp.home.data.remote

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.markusw.cosasdeunicorapp.R
import com.markusw.cosasdeunicorapp.core.presentation.UiText
import com.markusw.cosasdeunicorapp.core.utils.Result
import com.markusw.cosasdeunicorapp.home.domain.model.Message
import com.markusw.cosasdeunicorapp.home.domain.model.NotificationData
import com.markusw.cosasdeunicorapp.home.domain.model.PushNotification
import com.markusw.cosasdeunicorapp.home.domain.remote.PushNotificationService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

/**
 * Created by Markus on 3-12-2023.
 * Service that handles the sending of push notifications
 * @param api the api to send the notifications to
 * @see FirebaseCloudMessagingApi
 */
class FirebasePushNotificationService(
    private val api: FirebaseCloudMessagingApi,
    private val auth: FirebaseAuth,
    private val context: Context
): PushNotificationService {

    /**
     * Sends a push notification to the user that sent the message that was replied to
     * @param message the message that was replied to
     * @return a Result with the success or error
     */
    override suspend fun postNotification(message: Message): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                message.content.replyTo?.let { repliedMessage ->

                    val repliedMessageSenderId = repliedMessage.sender.uid

                    if (auth.uid == repliedMessageSenderId) {
                        Timber.d("Not sending notification because the user replied to their own message")
                        return@withContext Result.Success(Unit)
                    }

                    val notification = PushNotification(
                        data = NotificationData(
                            title = context.getString(
                                R.string.replied_text,
                                message.sender.displayName
                            ),
                            message = message.content.text,
                        ),
                        to = "/topics/${repliedMessageSenderId}"
                    )

                    val response = api.postNotification(notification)

                    if (!response.isSuccessful) {
                        Result.Error<Unit>(
                            UiText.DynamicString(
                                response.errorBody()?.string() ?: "null error"
                            )
                        )
                    }
                }
                Result.Success(Unit)
            } catch (e: Exception) {
                Timber.e(e)
                Result.Error(UiText.DynamicString("Error: ${e.message}"))
            }
        }

    }

}