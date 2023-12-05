package com.markusw.cosasdeunicorapp.di

import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.markusw.cosasdeunicorapp.core.data.FireStoreService
import com.markusw.cosasdeunicorapp.core.domain.RemoteDatabase
import com.markusw.cosasdeunicorapp.core.utils.Constants.PUSH_NOTIFICATION_API_BASE_URL
import com.markusw.cosasdeunicorapp.home.data.remote.PushNotificationApi
import com.markusw.cosasdeunicorapp.home.data.remote.PushNotificationService
import com.markusw.cosasdeunicorapp.home.data.repository.AndroidChatRepository
import com.markusw.cosasdeunicorapp.home.data.repository.FireStorePager
import com.markusw.cosasdeunicorapp.home.data.repository.FirebaseStorageService
import com.markusw.cosasdeunicorapp.home.domain.repository.ChatRepository
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
        pushNotificationService: PushNotificationService
    ): ChatRepository = AndroidChatRepository(remoteDatabase, pushNotificationService)

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

    @Provides
    @Singleton
    fun provideRemoteStorage(
        @ApplicationContext context: Context,
        storage: FirebaseStorage
    ): RemoteStorage = FirebaseStorageService(context, storage)

    @Provides
    @Singleton
    fun providePushNotificationApi(): PushNotificationApi {
        val retrofitClient = Retrofit
            .Builder()
            .baseUrl(PUSH_NOTIFICATION_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofitClient.create(PushNotificationApi::class.java)
    }

}