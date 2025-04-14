package com.buildingblocks.android.security.aichat.di

import com.buildingblocks.android.security.aichat.data.repository.ChatRepository
import com.buildingblocks.android.security.aichat.data.repository.ChatRepositoryImpl
import com.buildingblocks.android.security.aichat.data.repository.ConversationRepositoryImpl
import com.buildingblocks.android.security.aichat.domain.repository.ConversationRepository
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