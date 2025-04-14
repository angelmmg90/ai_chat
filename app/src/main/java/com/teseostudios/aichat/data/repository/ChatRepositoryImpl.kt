package com.teseostudios.aichat.data.repository

import com.teseostudios.aichat.data.network.OpenAIService
import com.teseostudios.aichat.domain.model.Message
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRepositoryImpl @Inject constructor(
    private val openAIService: OpenAIService
) : ChatRepository {
    
    override suspend fun sendMessage(content: String, conversation: List<Message>): Flow<ChatOperationResult> = flow {
        try {
            // Emitir estado de carga
            emit(ChatOperationResult.Loading)
            
            // Convertir los mensajes al formato esperado por el servicio
            val previousMessages = conversation.map { 
                Pair(it.content, it.isFromUser) 
            }
            
            // Obtener respuesta de la IA
            val aiResponse = openAIService.sendMessage(content, previousMessages)
            
            // Crear un mensaje con la respuesta
            val responseMessage = Message(
                content = aiResponse,
                isFromUser = false
            )
            
            // Emitir estado de éxito con el mensaje
            emit(ChatOperationResult.Success(responseMessage))
        } catch (e: Exception) {
            // Emitir estado de error
            emit(ChatOperationResult.Error("Error: ${e.localizedMessage}"))
        }
    }
} 