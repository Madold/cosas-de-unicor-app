package com.markusw.cosasdeunicorapp.core.data

import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.markusw.cosasdeunicorapp.R
import com.markusw.cosasdeunicorapp.core.domain.RemoteDatabase
import com.markusw.cosasdeunicorapp.core.domain.model.User
import com.markusw.cosasdeunicorapp.core.presentation.UiText
import com.markusw.cosasdeunicorapp.core.utils.Result
import com.markusw.cosasdeunicorapp.core.utils.TimeUtils
import com.markusw.cosasdeunicorapp.home.data.repository.MessageFireStorePager
import com.markusw.cosasdeunicorapp.home.data.repository.NewsFireStorePager
import com.markusw.cosasdeunicorapp.home.domain.model.Message
import com.markusw.cosasdeunicorapp.home.domain.model.News
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.tasks.await

class FireStoreService(
    private val fireStore: FirebaseFirestore,
    private val messagesPager: MessageFireStorePager,
    private val newsPager: NewsFireStorePager
) : RemoteDatabase {

    companion object {
        const val GLOBAL_CHAT_COLLECTION = "global_chat"
        const val USERS_COLLECTION = "users"
        const val TIMESTAMP = "timestamp"
        const val PAGE_SIZE = 10L
        const val NEWS_COLLECTION = "news"
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
            fireStore
                .collection(USERS_COLLECTION)
                .document(user.uid)
                .set(user)
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
                                val news = change.document.toObject(News::class.java)
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

}