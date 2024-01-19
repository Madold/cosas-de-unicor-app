package com.markusw.cosasdeunicorapp.core.data

import android.net.Uri
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.messaging.FirebaseMessaging
import com.markusw.cosasdeunicorapp.core.EmailNotVerifiedException
import com.markusw.cosasdeunicorapp.core.domain.AuthService
import com.markusw.cosasdeunicorapp.core.domain.ProfileUpdateData
import com.markusw.cosasdeunicorapp.core.domain.RemoteDatabase
import com.markusw.cosasdeunicorapp.core.ext.toDomainModel
import com.markusw.cosasdeunicorapp.core.utils.Result
import kotlinx.coroutines.tasks.await
import timber.log.Timber

/**
 * Created by Markus on 3-12-2023.
 * Concrete implementation of the AuthService interface for android
 * @param auth the firebase auth instance
 * @param remoteDatabase the remote database to get the data from
 * @param messaging the messaging service to subscribe to topics
 * @see AuthService
 * @see FirebaseAuth
 * @see RemoteDatabase
 * @see FirebaseMessaging
 */
class FirebaseAuthService(
    private val auth: FirebaseAuth,
    private val remoteDatabase: RemoteDatabase,
    private val messaging: FirebaseMessaging,
) : FirebaseService(), AuthService {

    override suspend fun authenticate(email: String, password: String): Result<Unit> {
        return executeFirebaseOperation {
            auth.signInWithEmailAndPassword(email, password).await()
            val loggedUser = auth.currentUser!!
            loggedUser.let {
                if (!it.isEmailVerified) {
                    auth.signOut()
                    throw EmailNotVerifiedException()
                }
            }
            messaging.subscribeToTopic("/topics/${loggedUser.uid}")
            messaging.subscribeToTopic("/topics/news")
        }
    }

    override suspend fun register(name: String, email: String, password: String): Result<Unit> {
        return executeFirebaseOperation {
            auth.createUserWithEmailAndPassword(email, password).await()
            val loggedUser = auth.currentUser!!
            updateUserProfileData(ProfileUpdateData(displayName = name))
            remoteDatabase.saveUserInDatabase(loggedUser.toDomainModel())
            sendEmailVerification(loggedUser)
            auth.signOut()
        }
    }

    override suspend fun updateUserProfileData(data: ProfileUpdateData): Result<Unit> {

        val (displayName, email, profilePhotoUri) = data

        return executeFirebaseOperation {
            auth.currentUser?.updateProfile(
                userProfileChangeRequest {
                    displayName?.let { this.displayName = it }
                    profilePhotoUri?.let { this.photoUri = Uri.parse(it) }
                }
            )?.await()

            email?.let { auth.currentUser?.updateEmail(it)?.await() }
            auth.currentUser?.reload()?.await()
            auth.currentUser?.let { remoteDatabase.updateUserInfo(it.toDomainModel()) }
        }
    }

    private suspend fun sendEmailVerification(user: FirebaseUser?) {
        user?.sendEmailVerification()?.await()
    }

    override suspend fun logout(): Result<Unit> {
        return executeFirebaseOperation {
            Timber.d("Started logout")
            val loggedUser = auth.currentUser!!
            messaging.unsubscribeFromTopic("/topics/${loggedUser.uid}")
            messaging.unsubscribeFromTopic("/topics/news")
            auth.signOut()
            Timber.d("finished logout")
        }
    }

    override suspend fun authenticateWithCredential(credential: AuthCredential): Result<Unit> {
        return executeFirebaseOperation {
            auth.signInWithCredential(credential).await()
            val loggedUser = auth.currentUser!!
            remoteDatabase.saveUserInDatabase(auth.currentUser!!.toDomainModel())
            messaging.subscribeToTopic("/topics/${loggedUser.uid}")
            messaging.subscribeToTopic("/topics/news")
        }
    }

    override suspend fun sendPasswordResetByEmail(email: String): Result<Unit> {
        return executeFirebaseOperation {
            auth.sendPasswordResetEmail(email).await()
        }
    }



}