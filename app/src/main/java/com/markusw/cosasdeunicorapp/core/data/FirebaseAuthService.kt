package com.markusw.cosasdeunicorapp.core.data

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.messaging.FirebaseMessaging
import com.markusw.cosasdeunicorapp.core.EmailNotVerifiedException
import com.markusw.cosasdeunicorapp.core.domain.AuthService
import com.markusw.cosasdeunicorapp.core.domain.ProfileUpdateData
import com.markusw.cosasdeunicorapp.core.domain.RemoteDatabase
import com.markusw.cosasdeunicorapp.core.domain.model.User
import com.markusw.cosasdeunicorapp.core.ext.toDomainModel
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
) : AuthService {

    override suspend fun authenticate(email: String, password: String) {
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

    override suspend fun register(name: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).await()
        val loggedUser = auth.currentUser!!
        updateUserProfileData(ProfileUpdateData(displayName = name))
        remoteDatabase.saveUserInDatabase(loggedUser.toDomainModel())
        sendEmailVerification(loggedUser)
        auth.signOut()
    }

    override suspend fun updateUserProfileData(data: ProfileUpdateData) {
        val (displayName, email) = data

        email?.let {
            auth.currentUser?.updateEmail(it)?.await()
        }
        displayName?.let {
            auth.currentUser?.updateProfile(
                userProfileChangeRequest {
                    this.displayName = it
                }
            )?.await()
        }
        auth.currentUser?.let { remoteDatabase.updateUserInfo(it.uid, data) }
    }

    override suspend fun getLoggedUser(): User {
        return remoteDatabase.getUser(auth.currentUser!!.uid)
    }

    private suspend fun sendEmailVerification(user: FirebaseUser?) {
        user?.sendEmailVerification()?.await()
    }

    override suspend fun logout() {
        Timber.d("Started logout")
        val loggedUser = auth.currentUser!!
        messaging.unsubscribeFromTopic("/topics/${loggedUser.uid}")
        messaging.unsubscribeFromTopic("/topics/news")
        auth.signOut()
        Timber.d("finished logout")
    }

    override suspend fun authenticateWithCredential(credential: AuthCredential) {
        auth.signInWithCredential(credential).await()
        val loggedUser = auth.currentUser!!
        remoteDatabase.saveUserInDatabase(auth.currentUser!!.toDomainModel())
        messaging.subscribeToTopic("/topics/${loggedUser.uid}")
        messaging.subscribeToTopic("/topics/news")
    }

    override suspend fun sendPasswordResetByEmail(email: String) {
        auth.sendPasswordResetEmail(email).await()
    }

}