package com.markusw.cosasdeunicorapp.di

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.markusw.cosasdeunicorapp.core.domain.AuthService
import com.markusw.cosasdeunicorapp.core.data.FireStoreService
import com.markusw.cosasdeunicorapp.core.data.FirebaseAuthService
import com.markusw.cosasdeunicorapp.core.domain.RemoteDatabase
import com.markusw.cosasdeunicorapp.home.data.repository.FireStorePager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
    fun provideRemoteDatabase(fireStore: FirebaseFirestore, fireStorePager: FireStorePager): RemoteDatabase = FireStoreService(fireStore, fireStorePager)

}