package com.teseostudios.aichat.data.mapper

import com.teseostudios.aichat.data.local.entity.ConversationEntity
import com.teseostudios.aichat.domain.model.Conversation

fun ConversationEntity.toDomain(messages: List<com.teseostudios.aichat.domain.model.Message> = emptyList()): Conversation {
    return Conversation(
        id = id,
        title = title,
        messages = messages,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun Conversation.toEntity(): ConversationEntity {
    return ConversationEntity(
        id = id,
        title = title,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
} 