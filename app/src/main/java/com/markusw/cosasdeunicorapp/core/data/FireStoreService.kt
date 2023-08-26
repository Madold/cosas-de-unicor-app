package com.markusw.cosasdeunicorapp.core.data

import com.google.firebase.firestore.FirebaseFirestore
import com.markusw.cosasdeunicorapp.R
import com.markusw.cosasdeunicorapp.core.domain.RemoteDatabase
import com.markusw.cosasdeunicorapp.core.utils.Resource
import com.markusw.cosasdeunicorapp.core.presentation.UiText
import com.markusw.cosasdeunicorapp.home.domain.model.Message
import com.markusw.cosasdeunicorapp.home.domain.model.User
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.tasks.await

class FireStoreService constructor(
    private val fireStore: FirebaseFirestore
) : RemoteDatabase {
    companion object {
        const val GLOBAL_CHAT_COLLECTION = "global_chat"
        const val USERS_COLLECTION = "users"
        const val TIMESTAMP = "timestamp"
    }

    override suspend fun getGlobalChatList() = callbackFlow<Resource<List<Message>>> {
        val snapshotListener =
            fireStore.collection(GLOBAL_CHAT_COLLECTION)
                .orderBy(TIMESTAMP)
                .addSnapshotListener { value, error ->
                    value?.let {
                        val messages = it
                            .documents
                            .asSequence()
                            .map { document ->
                                document.toObject(Message::class.java)!!
                            }.toList()
                        trySend(Resource.Success(messages))
                    } ?: run {
                        trySend(
                            Resource.Error(
                                UiText.StringResource(
                                    R.string.unknownException,
                                    "${error?.javaClass} ${error?.message}"
                                )
                            )
                        )
                    }
                }

        awaitClose {
            snapshotListener.remove()
        }
    }.conflate()

    override suspend fun sendMessageToGlobalChat(message: Message): Resource<Unit> {
        return try {
            fireStore.collection(GLOBAL_CHAT_COLLECTION).add(message).await()
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(
                UiText.StringResource(
                    R.string.unknownException,
                    "${e.javaClass}: ${e.message}"
                )
            )
        }
    }

    override suspend fun saveUserInDatabase(user: User): Resource<Unit> {
        return try {
            fireStore.collection(USERS_COLLECTION).document(user.uid).set(user).await()
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(
                UiText.StringResource(
                    R.string.unknownException,
                    "${e.javaClass}: ${e.message}"
                )
            )
        }
    }

}