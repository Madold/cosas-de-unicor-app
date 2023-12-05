package com.markusw.cosasdeunicorapp.home.data.remote

import com.markusw.cosasdeunicorapp.core.utils.Constants.PUSH_NOTIFICATION_SERVER_KEY
import com.markusw.cosasdeunicorapp.home.domain.model.PushNotification
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST


/**
 * Created by Markus on 3-12-2023.
 * Represents the api to send push notifications
 */
interface PushNotificationApi {

    /**
     * Sends a push notification to a device
     * @param notification the notification to send
     * @return the response of the server
     * @see PushNotification
     * @see Response
     * @see ResponseBody
     */
    @Headers(
        "Authorization: key=$PUSH_NOTIFICATION_SERVER_KEY",
        "Content-Type:application/json"
    )
    @POST("fcm/send")
    suspend fun postNotification(@Body notification: PushNotification): Response<ResponseBody>

}