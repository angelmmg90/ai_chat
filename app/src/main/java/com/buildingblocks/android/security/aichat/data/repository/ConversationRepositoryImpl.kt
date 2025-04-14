package com.buildingblocks.android.security.aichat.data.repository

import com.buildingblocks.android.security.aichat.data.local.dao.ConversationDao
import com.buildingblocks.android.security.aichat.data.local.dao.MessageDao
import com.buildingblocks.android.security.aichat.data.local.entity.ConversationEntity
import com.buildingblocks.android.security.aichat.data.mapper.toDomain
import com.buildingblocks.android.security.aichat.data.mapper.toEntity
import com.buildingblocks.android.security.aichat.domain.model.Conversation
import com.buildingblocks.android.security.aichat.domain.model.Message
import com.buildingblocks.android.security.aichat.domain.repository.ConversationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ConversationRepositoryImpl @Inject constructor(
    private val conversationDao: ConversationDao,
    private val messageDao: MessageDao
) : ConversationRepository {
    
    override fun getAllConversations(): Flow<List<Conversation>> {
        return conversationDao.getAllConversations().map { entities ->
            entities.map { it.toDomain() }
        }
    }
    
    override fun getConversationById(conversationId: String): Flow<Conversation?> {
        return conversationDao.getConversationById(conversationId).map { entity ->
            entity?.let {
                val messages = messageDao.getMessagesForConversation(conversationId)
                it.toDomain(emptyList()) // Inicialmente retornamos una conversación sin mensajes
            }
        }
    }
    
    override fun getMessagesForConversation(conversationId: String): Flow<List<Message>> {
        return messageDao.getMessagesForConversation(conversationId).map { entities ->
            entities.map { it.toDomain() }
        }
    }
    
    override suspend fun createConversation(title: String): String {
        val conversation = ConversationEntity(
            title = title
        )
        conversationDao.insertConversation(conversation)
        return conversation.id
    }
    
    override suspend fun addMessageToConversation(conversationId: String, message: Message) {
        val orderInConversation = messageDao.getMessageCountForConversation(conversationId)
        messageDao.insertMessage(message.toEntity(conversationId, orderInConversation))
        conversationDao.updateConversationTimestamp(conversationId)
    }
    
    override suspend fun addMessagesToConversation(conversationId: String, messages: List<Message>) {
        val startOrder = messageDao.getMessageCountForConversation(conversationId)
        val messageEntities = messages.mapIndexed { index, message ->
            message.toEntity(conversationId, startOrder + index)
        }
        messageDao.insertMessages(messageEntities)
        conversationDao.updateConversationTimestamp(conversationId)
    }
    
    override suspend fun deleteConversation(conversationId: String) {
        conversationDao.deleteConversation(conversationId)
    }
    
    override suspend fun updateConversationTitle(conversationId: String, title: String) {
        val conversation = conversationDao.getConversationById(conversationId).first()
        conversation?.let {
            val updated = ConversationEntity(
                id = it.id,
                title = title,
                createdAt = it.createdAt,
                updatedAt = System.currentTimeMillis()
            )
            conversationDao.updateConversation(updated)
        }
    }
} 