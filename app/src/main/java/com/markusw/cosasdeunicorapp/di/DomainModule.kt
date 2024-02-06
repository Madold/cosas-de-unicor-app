package com.markusw.cosasdeunicorapp.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.markusw.cosasdeunicorapp.BuildConfig
import com.markusw.cosasdeunicorapp.core.data.FakeRemoteDatabase
import com.markusw.cosasdeunicorapp.core.data.FireStoreService
import com.markusw.cosasdeunicorapp.core.data.FirebaseAuthService
import com.markusw.cosasdeunicorapp.core.domain.AuthService
import com.markusw.cosasdeunicorapp.core.domain.RemoteDatabase
import com.markusw.cosasdeunicorapp.home.data.repository.MessageFireStorePager
import com.markusw.cosasdeunicorapp.home.data.repository.NewsFireStorePager
import com.markusw.cosasdeunicorapp.home.domain.repository.RemoteStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DomainModule {

    @Provides
    @Singleton
    fun provideAuthService(
        auth: FirebaseAuth,
        remoteDatabase: RemoteDatabase,
        messaging: FirebaseMessaging,
    ): AuthService = FirebaseAuthService(
        auth,
        remoteDatabase,
        messaging
    )

    @Provides
    @Singleton
    fun provideRemoteDatabase(
        fireStore: FirebaseFirestore,
        messagesFireStorePager: MessageFireStorePager,
        newsFireStorePager: NewsFireStorePager,
        auth: FirebaseAuth,
        remoteStorage: RemoteStorage
    ): RemoteDatabase {

        if (BuildConfig.BUILD_TYPE == "development") {
            return FakeRemoteDatabase()
        }

        return FireStoreService(
            fireStore,
            messagesFireStorePager,
            newsFireStorePager,
            auth,
            remoteStorage
        )
    }
}