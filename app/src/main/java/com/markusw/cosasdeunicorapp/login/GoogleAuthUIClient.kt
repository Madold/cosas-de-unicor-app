package com.markusw.cosasdeunicorapp.login

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInRequest.GoogleIdTokenRequestOptions
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.markusw.cosasdeunicorapp.R
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import java.util.concurrent.CancellationException

class GoogleAuthUIClient(
    private val context: Context,
    private val oneTapClient: SignInClient
) {
    suspend fun signIn(): IntentSender? {
        val result = try {
            oneTapClient.beginSignIn(
                buildSignInRequest()
            ).await()

        } catch (e: Exception) {
            Timber.e(e)
            if (e is CancellationException) throw e
            null
        }

        return result?.pendingIntent?.intentSender
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

}