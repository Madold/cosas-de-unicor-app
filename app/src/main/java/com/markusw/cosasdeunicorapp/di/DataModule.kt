package com.markusw.cosasdeunicorapp.di

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.markusw.cosasdeunicorapp.core.data.FireStoreService
import com.markusw.cosasdeunicorapp.core.domain.RemoteDatabase
import com.markusw.cosasdeunicorapp.home.data.repository.AndroidChatRepository
import com.markusw.cosasdeunicorapp.home.data.repository.FireStorePager
import com.markusw.cosasdeunicorapp.home.domain.repository.ChatRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideChatRepository(remoteDatabase: RemoteDatabase): ChatRepository = AndroidChatRepository(remoteDatabase)

    @Provides
    @Singleton
    fun provideInitialQuery(fireStore: FirebaseFirestore): Query {
        return fireStore
            .collection(FireStoreService.GLOBAL_CHAT_COLLECTION)
            .limit(FireStoreService.PAGE_SIZE)
            .orderBy(FireStoreService.TIMESTAMP, Query.Direction.DESCENDING)
    }

    @Provides
    @Singleton
    fun provideFireStorePager(initialQuery: Query): FireStorePager = FireStorePager(initialQuery)


}