package com.markusw.cosasdeunicorapp.core.data

import android.net.Uri
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.messaging.FirebaseMessaging
import com.markusw.cosasdeunicorapp.R
import com.markusw.cosasdeunicorapp.core.domain.AuthService
import com.markusw.cosasdeunicorapp.core.domain.RemoteDatabase
import com.markusw.cosasdeunicorapp.core.ext.toDomainModel
import com.markusw.cosasdeunicorapp.core.presentation.UiText
import com.markusw.cosasdeunicorapp.core.utils.Result
import kotlinx.coroutines.tasks.await

class FirebaseAuthService(
    private val auth: FirebaseAuth,
    private val remoteDatabase: RemoteDatabase,
    private val messaging: FirebaseMessaging
) : AuthService {

    override suspend fun authenticate(email: String, password: String): Result<Unit> {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()

            auth.currentUser?.let {
                if (!it.isEmailVerified) {
                    auth.signOut()
                    return Result.Error(UiText.StringResource(R.string.account_not_verified))
                }
            }

            Result.Success(Unit)
        } catch (e: FirebaseAuthInvalidUserException) {
            Result.Error(UiText.StringResource(R.string.user_not_exist))
        } catch (e: Exception) {
            Result.Error(
                UiText.StringResource(
                    R.string.unknownException,
                    "${e.javaClass}: ${e.message}"
                )
            )
        }
    }

    override suspend fun register(name: String, email: String, password: String): Result<Unit> {
        return try {
            val loggedUser = auth.currentUser!!
            auth.createUserWithEmailAndPassword(email, password).await()
            updateUserProfileData(displayName = name)
            val registerResult = remoteDatabase.saveUserInDatabase(loggedUser.toDomainModel())
            sendEmailVerification(loggedUser)

            if (registerResult is Result.Error) {
                return Result.Error(registerResult.message!!)
            }

            auth.signOut()
            Result.Success(Unit)
        } catch (e: FirebaseAuthUserCollisionException) {
            Result.Error(UiText.StringResource(R.string.user_already_registered))
        } catch (e: Exception) {
            Result.Error(
                UiText.StringResource(
                    R.string.unknownException,
                    "${e.javaClass}: ${e.message}"
                )
            )
        }
    }

    private suspend fun updateUserProfileData(displayName: String? = null, photoUri: Uri? = null) {
        auth.currentUser?.updateProfile(
            userProfileChangeRequest {
                displayName?.let { this.displayName = it }
                photoUri?.let { this.photoUri = it }
            }
        )?.await()
    }

    private suspend fun sendEmailVerification(user: FirebaseUser?) {
        user?.sendEmailVerification()?.await()
    }

    override suspend fun logout(): Result<Unit> {
        return try {
            auth.signOut()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(
                UiText.StringResource(
                    R.string.unknownException,
                    "${e.javaClass}: ${e.message}"
                )
            )
        }
    }

    override suspend fun authenticateWithCredential(credential: AuthCredential): Result<Unit> {
        return try {
            auth.signInWithCredential(credential).await()
            val registerResult =
                remoteDatabase.saveUserInDatabase(auth.currentUser!!.toDomainModel())

            if (registerResult is Result.Error) {
                return Result.Error(registerResult.message!!)
            }

            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(
                UiText.StringResource(
                    R.string.unknownException,
                    "${e.javaClass}: ${e.message}"
                )
            )
        }
    }

    override suspend fun sendPasswordResetByEmail(email: String): Result<Unit> {
        return try {
            auth.sendPasswordResetEmail(email).await()
            Result.Success(Unit)
        } catch (e: FirebaseAuthInvalidUserException) {
            Result.Error(UiText.StringResource(R.string.user_not_exist))
        } catch (e: Exception) {
            Result.Error(
                UiText.StringResource(
                    R.string.unknownException,
                    "${e.javaClass}: ${e.message}"
                )
            )
        }
    }

}