package com.markusw.cosasdeunicorapp.auth.presentation.login

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInRequest.GoogleIdTokenRequestOptions
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.UnsupportedApiCallException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.markusw.cosasdeunicorapp.R
import com.markusw.cosasdeunicorapp.core.utils.Result
import com.markusw.cosasdeunicorapp.core.presentation.UiText
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import java.util.concurrent.CancellationException

class GoogleAuthUIClient(
    private val context: Context,
    private val oneTapClient: SignInClient
) {
    suspend fun signIn(): Result<IntentSender?> {
        return try {
            val result = oneTapClient.beginSignIn(
                buildSignInRequest()
            ).await()

            Result.Success(
                data = result?.pendingIntent?.intentSender
            )
        } catch (e: ApiException) {
            Timber.e(e)
            handleStatusCode(e.statusCode, e)
        } catch (e: UnsupportedApiCallException) {
            Result.Error(UiText.StringResource(R.string.unsuportedApiException))
        } catch (e: Exception) {
            Timber.e(e)
            if (e is CancellationException) throw e
            Result.Error(
                UiText.StringResource(
                    R.string.unknownException,
                    "${e.javaClass} ${e.message}"
                )
            )
        }

    }

    fun getGoogleCredentialsFromIntent(intent: Intent?): AuthCredential {
        val credential = oneTapClient.getSignInCredentialFromIntent(intent)
        val googleIdToken = credential.googleIdToken
        return GoogleAuthProvider.getCredential(googleIdToken, null)
    }

    private fun buildSignInRequest(): BeginSignInRequest {
        return BeginSignInRequest.Builder()
            .setGoogleIdTokenRequestOptions(
                GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(context.getString(R.string.default_web_client_id))
                    .build()

            )
            .setAutoSelectEnabled(false)
            .build()
    }

    private fun handleStatusCode(
        code: Int,
        exception: ApiException
    ): Result.Error<IntentSender?> {
        return when (code) {
            CommonStatusCodes.NETWORK_ERROR -> {
                Result.Error(UiText.StringResource(R.string.network_error))
            }

            else -> {
                Timber.d("Unknown error code: $code: ${exception.message}")
                Result.Error(
                    UiText.StringResource(
                        R.string.unknownException,
                        "${exception.javaClass} ${exception.message}"
                    )
                )
            }
        }
    }

}