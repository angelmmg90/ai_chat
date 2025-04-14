package com.buildingblocks.android.security.aichat.data.repository

import com.buildingblocks.android.security.aichat.domain.model.Message
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    /**
     * Envía un mensaje a la IA y obtiene una respuesta.
     * 
     * @param content Contenido del mensaje a enviar
     * @param conversation Lista de mensajes anteriores para dar contexto
     * @return Flujo que emite el estado de la operación (cargando, éxito, error)
     */
    suspend fun sendMessage(content: String, conversation: List<Message>): Flow<ChatOperationResult>
}

sealed class ChatOperationResult {
    data object Loading : ChatOperationResult()
    data class Success(val message: Message) : ChatOperationResult()
    data class Error(val message: String) : ChatOperationResult()
} 