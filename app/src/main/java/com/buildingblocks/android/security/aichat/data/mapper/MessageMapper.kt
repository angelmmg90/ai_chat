package com.buildingblocks.android.security.aichat.data.mapper

import com.buildingblocks.android.security.aichat.data.local.entity.MessageEntity
import com.buildingblocks.android.security.aichat.domain.model.Message

fun MessageEntity.toDomain(): Message {
    return Message(
        id = id,
        content = content,
        isFromUser = isFromUser
    )
}

fun Message.toEntity(conversationId: String, orderInConversation: Int): MessageEntity {
    return MessageEntity(
        id = id,
        conversationId = conversationId,
        content = content,
        isFromUser = isFromUser,
        orderInConversation = orderInConversation
    )
} 