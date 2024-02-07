package com.markusw.cosasdeunicorapp.core.presentation

import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.FirebaseNetworkException
import com.markusw.cosasdeunicorapp.R
import com.markusw.cosasdeunicorapp.core.utils.Result
import kotlinx.coroutines.tasks.await

class GoogleAuthClient(
    val context: Context
) {

    private val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(context.getString(R.string.default_web_client_id))
        .requestEmail()
        .build()

    private val client = GoogleSignIn.getClient(context, signInOptions)

    fun signIn(): Result<Intent> {
        return try {
            Result.Success(client.signInIntent)
        } catch (e: Exception) {
            Result.Error(
                UiText.DynamicString(
                    "${e.javaClass} ${e.message}"
                )
            )
        }
    }

    suspend fun signOut() {
        client.signOut().await()
    }

}