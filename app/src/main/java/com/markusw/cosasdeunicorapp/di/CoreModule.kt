package com.markusw.cosasdeunicorapp.di

import android.content.Context
import com.markusw.cosasdeunicorapp.core.DispatcherProvider
import com.markusw.cosasdeunicorapp.core.data.AndroidSoundPlayer
import com.markusw.cosasdeunicorapp.core.domain.SoundPlayer
import com.markusw.cosasdeunicorapp.core.utils.DefaultDispatchers
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoreModule {

    @Provides
    @Singleton
    fun provideDispatcherProvider(): DispatcherProvider = DefaultDispatchers()

    @Provides
    @Singleton
    fun provideSoundPlayer(
        @ApplicationContext context: Context
    ): SoundPlayer = AndroidSoundPlayer(context)

}