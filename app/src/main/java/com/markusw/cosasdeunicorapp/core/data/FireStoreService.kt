package com.markusw.cosasdeunicorapp.core.data

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.markusw.cosasdeunicorapp.R
import com.markusw.cosasdeunicorapp.core.domain.ProfileUpdateData
import com.markusw.cosasdeunicorapp.core.domain.RemoteDatabase
import com.markusw.cosasdeunicorapp.core.domain.model.User
import com.markusw.cosasdeunicorapp.core.presentation.UiText
import com.markusw.cosasdeunicorapp.core.utils.Result
import com.markusw.cosasdeunicorapp.core.utils.TimeUtils
import com.markusw.cosasdeunicorapp.home.data.repository.MessageFireStorePager
import com.markusw.cosasdeunicorapp.home.data.repository.NewsFireStorePager
import com.markusw.cosasdeunicorapp.home.domain.model.Message
import com.markusw.cosasdeunicorapp.home.domain.model.News
import com.markusw.cosasdeunicorapp.home.domain.repository.RemoteStorage
import com.markusw.cosasdeunicorapp.tabulator.domain.model.AcademicProgram
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await

class FireStoreService(
    private val fireStore: FirebaseFirestore,
    private val messagesPager: MessageFireStorePager,
    private val newsPager: NewsFireStorePager,
    private val auth: FirebaseAuth,
    private val remoteStorage: RemoteStorage
) : RemoteDatabase {

    companion object {
        const val GLOBAL_CHAT_COLLECTION = "global_chat"
        const val USERS_COLLECTION = "users"
        const val TIMESTAMP = "timestamp"
        const val PAGE_SIZE = 10L
        const val NEWS_COLLECTION = "news"
        const val ACADEMIC_PROGRAMS_COLLECTION = "academic_programs"
    }

    override suspend fun loadPreviousMessages(): List<Message> {
        return messagesPager.loadPage()
    }

    private val systemTimeStamp = TimeUtils.getDeviceHourInTimestamp()

    override suspend fun sendMessageToGlobalChat(message: Message): Result<Unit> {
        return try {
            fireStore
                .collection(GLOBAL_CHAT_COLLECTION)
                .add(message)
                .await()
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

    override suspend fun saveUserInDatabase(user: User): Result<Unit> {
        return try {
            val documentReference = fireStore
                .collection(USERS_COLLECTION)
                .document(user.uid)
            val userExist = documentReference
                .get()
                .await()
                .data != null

            if (userExist) return Result.Success(Unit)

            documentReference
                .set(user)
                .await()
            return Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(
                UiText.StringResource(
                    R.string.unknownException,
                    "${e.javaClass}: ${e.message}"
                )
            )
        }
    }

    override suspend fun onNewMessage(): Flow<Message> = callbackFlow {
        val snapshotListener = fireStore
            .collection(GLOBAL_CHAT_COLLECTION)
            .addSnapshotListener { value, error ->

                error?.let {
                    close(it)
                    return@addSnapshotListener
                }

                value?.let {
                    it.documentChanges.forEach { change ->
                        when (change.type) {
                            DocumentChange.Type.ADDED -> {
                                val message = change.document.toObject(Message::class.java)
                                if (message.timestamp > systemTimeStamp) {
                                    trySend(message)
                                }
                            }

                            else -> {}
                        }
                    }
                }
            }

        awaitClose {
            snapshotListener.remove()
        }

    }.conflate()

    override suspend fun loadPreviousNews(): List<News> {
        return newsPager.loadPage()
    }

    override suspend fun onNewNews(): Flow<News> = callbackFlow {
        val snapshotListener = fireStore
            .collection(NEWS_COLLECTION)
            .addSnapshotListener { value, error ->
                error?.let {
                    close(it)
                }

                value?.let {
                    it.documentChanges.forEach { change ->
                        when (change.type) {
                            DocumentChange.Type.ADDED -> {
                                val news = change
                                    .document
                                    .toObject(News::class.java)
                                    .copy(id = change.document.id)

                                if (news.timestamp > systemTimeStamp) {
                                    trySend(news)
                                }
                            }

                            else -> {}
                        }
                    }
                }
            }

        awaitClose {
            snapshotListener.remove()
        }

    }.conflate()

    override suspend fun removeUserFromLikedByList(newsId: String, userId: String): Result<Unit> {
        return try {

            fireStore
                .collection(NEWS_COLLECTION)
                .document(newsId)
                .update(
                    mapOf(
                        "likedBy" to FieldValue.arrayRemove(userId)
                    )
                )
                .await()

            Result.Success(Unit)

        } catch (e: Exception) {
            Result.Error(
                UiText.DynamicString(
                    "${e.javaClass}: ${e.message}"
                )
            )
        }

    }

    override suspend fun addUserToLikedByList(newsId: String, userId: String): Result<Unit> {
        return try {
            fireStore
                .collection(NEWS_COLLECTION)
                .document(newsId)
                .update(
                    mapOf(
                        "likedBy" to FieldValue.arrayUnion(userId)
                    )
                )
                .await()

            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(
                UiText.DynamicString(
                    "${e.javaClass}: ${e.message}"
                )
            )
        }
    }

    override suspend fun getUsersCount(): Result<Int> {
        return try {
            val usersCount = fireStore
                .collection(USERS_COLLECTION)
                .get()
                .await()
                .toObjects(User::class.java)
                .size

            Result.Success(usersCount)

        } catch (e: Exception) {
            Result.Error(
                UiText.DynamicString(
                    "${e.javaClass}: ${e.message}"
                )
            )
        }
    }

    override suspend fun updateUserInfo(id: String, data: ProfileUpdateData): Result<Unit> {

        return try {

            val (displayName, email, profilePhotoUri) = data

            val user = fireStore
                .collection(USERS_COLLECTION)
                .document(id)
                .get()
                .await()
                .toObject(User::class.java)!!

            val updatedUser = user.copy(
                displayName = displayName ?: user.displayName,
                email = email ?: user.email,
                photoUrl = profilePhotoUri?.let {
                    remoteStorage.uploadImage(
                        Uri.parse(
                            profilePhotoUri
                        )
                    ).data
                } ?: user.photoUrl
            )

            fireStore
                .collection(USERS_COLLECTION)
                .document(id)
                .set(updatedUser)
                .await()

            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(
                UiText.DynamicString(
                    "${e.javaClass}: ${e.message}"
                )
            )
        }

    }


    override suspend fun onUserInfoUpdate(): Flow<User> {
        return callbackFlow {
            val snapshotListener = fireStore
                .collection(USERS_COLLECTION)
                .document(auth.currentUser!!.uid)
                .addSnapshotListener { value, error ->
                    error?.let {
                        close(it)
                    }

                    value?.let {
                        val user = it.toObject(User::class.java)!!
                        trySend(user)
                    }
                }

            awaitClose {
                snapshotListener.remove()
            }
        }.conflate()
    }

    override suspend fun getUser(id: String): User {
        return fireStore
            .collection(USERS_COLLECTION)
            .document(id)
            .get()
            .await()
            .toObject(User::class.java)!!
    }

    override fun getAcademicPrograms(): Flow<List<AcademicProgram>> {
        return callbackFlow {
            val snapshotListener = fireStore
                .collection(ACADEMIC_PROGRAMS_COLLECTION)
                .addSnapshotListener { value, error ->
                    error?.let { close(it) }

                    value?.let { academicPrograms ->
                        trySend(academicPrograms.toObjects(AcademicProgram::class.java))
                    }
                }
            awaitClose {
                snapshotListener.remove()
            }
        }.conflate().flowOn(Dispatchers.IO)
    }

}