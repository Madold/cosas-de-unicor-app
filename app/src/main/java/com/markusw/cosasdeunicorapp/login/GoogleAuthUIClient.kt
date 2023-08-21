package com.markusw.cosasdeunicorapp.login

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInRequest.GoogleIdTokenRequestOptions
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.UnsupportedApiCallException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.markusw.cosasdeunicorapp.R
import com.markusw.cosasdeunicorapp.core.utils.Resource
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import java.util.concurrent.CancellationException

class GoogleAuthUIClient(
    private val context: Context,
    private val oneTapClient: SignInClient
) {
    suspend fun signIn(): Resource<IntentSender?> {
        return try {
            val result =  oneTapClient.beginSignIn(
                buildSignInRequest()
            ).await()

            Resource.Success(
                data = result?.pendingIntent?.intentSender
            )
        } catch (e: ApiException) {
            Timber.e(e)
          Resource.Error("${e.javaClass} Al parecer has intentado iniciar sesión muchas veces con google y has cancelado, este metodo de inicio de sesión ha sido bloqueado temporalmente, intenta de nuevo en 24 horas.")
        } catch (e: UnsupportedApiCallException) {
            Resource.Error("Al parecer no tienes google play services instalado o actualizado. No puedes iniciar sesión con google si falta este servicio.")
        } catch (e: Exception) {
            Timber.e(e)
            if (e is CancellationException) throw e
            Resource.Error("${e.javaClass}: ${e.message}")
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

}