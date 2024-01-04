package com.markusw.cosasdeunicorapp.di

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.markusw.cosasdeunicorapp.core.data.FireStoreService
import com.markusw.cosasdeunicorapp.core.domain.RemoteDatabase
import com.markusw.cosasdeunicorapp.core.utils.Constants.PUSH_NOTIFICATION_API_BASE_URL
import com.markusw.cosasdeunicorapp.home.data.remote.FirebasePushNotificationService
import com.markusw.cosasdeunicorapp.home.data.remote.FirebaseCloudMessagingApi
import com.markusw.cosasdeunicorapp.home.data.repository.AndroidChatRepository
import com.markusw.cosasdeunicorapp.home.data.repository.AndroidNewsRepository
import com.markusw.cosasdeunicorapp.home.data.repository.MessageFireStorePager
import com.markusw.cosasdeunicorapp.home.data.repository.FirebaseStorageService
import com.markusw.cosasdeunicorapp.home.data.repository.NewsFireStorePager
import com.markusw.cosasdeunicorapp.home.domain.remote.PushNotificationService
import com.markusw.cosasdeunicorapp.home.domain.repository.ChatRepository
import com.markusw.cosasdeunicorapp.home.domain.repository.NewsRepository
import com.markusw.cosasdeunicorapp.home.domain.repository.RemoteStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideChatRepository(
        remoteDatabase: RemoteDatabase,
    ): ChatRepository = AndroidChatRepository(remoteDatabase)

    @Provides
    @Singleton
    fun provideNewsRepository(
        remoteDatabase: RemoteDatabase,
    ): NewsRepository = AndroidNewsRepository(remoteDatabase)

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
    fun provideFireStorePager(initialQuery: Query): MessageFireStorePager = MessageFireStorePager(initialQuery)

    @Provides
    @Singleton
    fun provideNewsPager(fireStore: FirebaseFirestore): NewsFireStorePager {
        val initialQuery = fireStore
            .collection(FireStoreService.NEWS_COLLECTION)
            .limit(FireStoreService.PAGE_SIZE)
            .orderBy(FireStoreService.TIMESTAMP, Query.Direction.DESCENDING)

        return NewsFireStorePager(initialQuery)
    }

    @Provides
    @Singleton
    fun provideRemoteStorage(
        @ApplicationContext context: Context,
        storage: FirebaseStorage
    ): RemoteStorage = FirebaseStorageService(
        context,
        storage
    )

    @Provides
    @Singleton
    fun providePushNotificationApi(): FirebaseCloudMessagingApi {
        val retrofitClient = Retrofit
            .Builder()
            .baseUrl(PUSH_NOTIFICATION_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofitClient.create(FirebaseCloudMessagingApi::class.java)
    }

    @Provides
    @Singleton
    fun providePushNotificationService(
        api: FirebaseCloudMessagingApi,
        auth: FirebaseAuth,
        @ApplicationContext context: Context
    ): PushNotificationService = FirebasePushNotificationService(
        api,
        auth,
        context
    )

}