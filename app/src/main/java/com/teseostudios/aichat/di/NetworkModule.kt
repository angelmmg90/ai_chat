package com.teseostudios.aichat.di

import com.aallam.openai.client.OpenAI
import com.teseostudios.aichat.BuildConfig
import com.teseostudios.aichat.data.network.OpenAIService
import com.teseostudios.aichat.data.network.OpenAIServiceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOpenAIClient(): OpenAI {
        return OpenAI(BuildConfig.OPENAI_API_KEY)
    }
    
    @Provides
    @Singleton
    fun provideOpenAIService(openAI: OpenAI): OpenAIService {
        return OpenAIServiceImpl(openAI)
    }
} 