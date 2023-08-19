package com.markusw.cosasdeunicorapp.di

import com.markusw.cosasdeunicorapp.data.ChatRepository
import com.markusw.cosasdeunicorapp.data.AndroidChatRepository
import com.markusw.cosasdeunicorapp.domain.services.RemoteDatabase
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

}