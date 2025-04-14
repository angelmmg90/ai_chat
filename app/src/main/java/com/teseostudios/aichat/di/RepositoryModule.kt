package com.teseostudios.aichat.di

import com.teseostudios.aichat.data.repository.ChatRepository
import com.teseostudios.aichat.data.repository.ChatRepositoryImpl
import com.teseostudios.aichat.data.repository.ConversationRepositoryImpl
import com.teseostudios.aichat.domain.repository.ConversationRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    
    @Binds
    @Singleton
    abstract fun bindConversationRepository(
        conversationRepositoryImpl: ConversationRepositoryImpl
    ): ConversationRepository

    @Binds
    @Singleton  // If you want a single instance
    abstract fun bindChatRepository(
        defaultChatRepository: ChatRepositoryImpl
    ): ChatRepository
} 