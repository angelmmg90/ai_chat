package com.buildingblocks.android.security.aichat.data.mapper

import com.buildingblocks.android.security.aichat.data.local.entity.ConversationEntity
import com.buildingblocks.android.security.aichat.domain.model.Conversation

fun ConversationEntity.toDomain(messages: List<com.buildingblocks.android.security.aichat.domain.model.Message> = emptyList()): Conversation {
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