package com.teseostudios.aichat.domain.repository

import com.teseostudios.aichat.domain.model.Conversation
import com.teseostudios.aichat.domain.model.Message
import kotlinx.coroutines.flow.Flow

interface ConversationRepository {
    fun getAllConversations(): Flow<List<Conversation>>
    fun getConversationById(conversationId: String): Flow<Conversation?>
    fun getMessagesForConversation(conversationId: String): Flow<List<Message>>
    suspend fun createConversation(title: String): String
    suspend fun addMessageToConversation(conversationId: String, message: Message)
    suspend fun addMessagesToConversation(conversationId: String, messages: List<Message>)
    suspend fun deleteConversation(conversationId: String)
    suspend fun updateConversationTitle(conversationId: String, title: String)
} 