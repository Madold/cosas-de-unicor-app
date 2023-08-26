package com.markusw.cosasdeunicorapp.di

import com.markusw.cosasdeunicorapp.home.domain.repository.ChatRepository
import com.markusw.cosasdeunicorapp.home.data.repository.AndroidChatRepository
import com.markusw.cosasdeunicorapp.core.domain.RemoteDatabase
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