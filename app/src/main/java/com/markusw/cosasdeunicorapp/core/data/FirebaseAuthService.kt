package com.markusw.cosasdeunicorapp.core.data

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.markusw.cosasdeunicorapp.R
import com.markusw.cosasdeunicorapp.core.domain.AuthService
import com.markusw.cosasdeunicorapp.core.ext.toUserModel
import com.markusw.cosasdeunicorapp.core.utils.Result
import com.markusw.cosasdeunicorapp.core.presentation.UiText
import com.markusw.cosasdeunicorapp.core.domain.RemoteDatabase
import kotlinx.coroutines.tasks.await

class FirebaseAuthService constructor(
    private val auth: FirebaseAuth,
    private val remoteDatabase: RemoteDatabase
) : AuthService {
    override suspend fun authenticate(email: String, password: String): Result<Unit> {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()

            auth.currentUser?.let {
                if (!it.isEmailVerified) {
                    it.sendEmailVerification().await()
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
            auth.createUserWithEmailAndPassword(email, password).await()
            setDisplayName(name)
            val registerResult = remoteDatabase.saveUserInDatabase(auth.currentUser!!.toUserModel())
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

    private suspend fun setDisplayName(name: String) {
        auth.currentUser!!.updateProfile(
            userProfileChangeRequest {
                displayName = name
            }
        ).await()
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
            val registerResult = remoteDatabase.saveUserInDatabase(auth.currentUser!!.toUserModel())

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