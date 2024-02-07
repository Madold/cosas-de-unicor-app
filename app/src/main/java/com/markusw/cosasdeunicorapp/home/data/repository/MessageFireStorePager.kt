package com.markusw.cosasdeunicorapp.home.data.repository

import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.markusw.cosasdeunicorapp.home.domain.model.Message
import com.markusw.cosasdeunicorapp.home.domain.repository.PaginationSource
import kotlinx.coroutines.tasks.await
class MessageFireStorePager(
    initialQuery: Query
) : PaginationSource<QuerySnapshot, Message> {

    private var currentPage = initialQuery
    override suspend fun loadPage(): List<Message> {
        val messages = currentPage.get().await()
        val lastVisibleMessage = messages.documents.lastOrNull()
        lastVisibleMessage?.let {
            val previousMessages = currentPage
                .startAfter(it)
                .get()
                .await()
            currentPage = previousMessages.query
            return messages.toObjects(Message::class.java)
        } ?: run {
            return emptyList()
        }
    }
}