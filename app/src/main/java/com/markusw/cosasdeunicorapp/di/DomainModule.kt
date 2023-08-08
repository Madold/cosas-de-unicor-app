package com.markusw.cosasdeunicorapp.di

import com.markusw.cosasdeunicorapp.domain.services.AuthService
import com.markusw.cosasdeunicorapp.domain.services.FirebaseAuthService
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
    fun provideAuthService(): AuthService = FirebaseAuthService()

}