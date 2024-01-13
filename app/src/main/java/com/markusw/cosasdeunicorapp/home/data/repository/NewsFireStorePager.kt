package com.markusw.cosasdeunicorapp.home.data.repository

import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.markusw.cosasdeunicorapp.home.domain.model.News
import com.markusw.cosasdeunicorapp.home.domain.repository.PaginationSource
import kotlinx.coroutines.tasks.await
import timber.log.Timber

class NewsFireStorePager(
    initialQuery: Query
) : PaginationSource<QuerySnapshot, News> {

    private var currentPage = initialQuery

    override suspend fun loadPage(): List<News> {
        val newsPage = currentPage.get().await()
        val lastNews = newsPage.documents.lastOrNull()

        lastNews?.let {
            val previousNews = currentPage
                .startAfter(it)
                .get()
                .await()
            currentPage = previousNews.query
            return newsPage.map { document ->
                document.toObject(News::class.java).copy(id = document.id)
            }
        } ?: run {
            return emptyList()
        }

    }
}