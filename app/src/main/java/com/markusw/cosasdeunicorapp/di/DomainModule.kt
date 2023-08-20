package com.markusw.cosasdeunicorapp.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.markusw.cosasdeunicorapp.domain.services.AuthService
import com.markusw.cosasdeunicorapp.domain.services.FireStoreService
import com.markusw.cosasdeunicorapp.domain.services.FirebaseAuthService
import com.markusw.cosasdeunicorapp.domain.services.RemoteDatabase
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
    fun provideAuthService(auth: FirebaseAuth, remoteDatabase: RemoteDatabase): AuthService = FirebaseAuthService(auth, remoteDatabase)

    @Provides
    @Singleton
    fun provideRemoteDatabase(fireStore: FirebaseFirestore): RemoteDatabase = FireStoreService(fireStore)

}