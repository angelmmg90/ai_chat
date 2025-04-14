package com.buildingblocks.android.security.aichat.di

import android.content.Context
import com.buildingblocks.android.security.aichat.data.local.AppDatabase
import com.buildingblocks.android.security.aichat.data.local.dao.ConversationDao
import com.buildingblocks.android.security.aichat.data.local.dao.MessageDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }
    
    @Provides
    fun provideConversationDao(appDatabase: AppDatabase): ConversationDao {
        return appDatabase.conversationDao()
    }
    
    @Provides
    fun provideMessageDao(appDatabase: AppDatabase): MessageDao {
        return appDatabase.messageDao()
    }
} 