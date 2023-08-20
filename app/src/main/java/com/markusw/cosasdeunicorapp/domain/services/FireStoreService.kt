package com.markusw.cosasdeunicorapp.domain.services

import com.google.firebase.firestore.FirebaseFirestore
import com.markusw.cosasdeunicorapp.core.utils.Resource
import com.markusw.cosasdeunicorapp.data.model.Message
import com.markusw.cosasdeunicorapp.data.model.User
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
    }

    override suspend fun getGlobalChatList() = callbackFlow<Resource<List<Message>>> {
        val snapshotListener =
            fireStore.collection(GLOBAL_CHAT_COLLECTION).addSnapshotListener { value, error ->
                value?.let {
                    val messages = it
                        .documents
                        .asSequence()
                        .map { document ->
                            document.toObject(Message::class.java)!!
                        }.toList()
                    trySend(Resource.Success(messages))
                } ?: run {
                    trySend(Resource.Error(error?.message.toString()))
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
            Resource.Error(e.message.toString())
        }
    }

    override suspend fun saveUserInDatabase(user: User): Resource<Unit> {
        return try {
            fireStore.collection(USERS_COLLECTION).document(user.uid).set(user).await()
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message.toString())
        }
    }

}