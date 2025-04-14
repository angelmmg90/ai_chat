package com.buildingblocks.android.security.aichat.data.network

import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementación del servicio OpenAIService que se conecta con la API de OpenAI
 */
@Singleton
class OpenAIServiceImpl @Inject constructor(
    private val openAI: OpenAI
) : OpenAIService {
    
    override suspend fun sendMessage(prompt: String, previousMessages: List<Pair<String, Boolean>>): String {
        try {
            val chatMessages = previousMessages.map { (content, isFromUser) ->
                ChatMessage(
                    role = if (isFromUser) ChatRole.User else ChatRole.Assistant,
                    content = content
                )
            }
            
            val messages = chatMessages + ChatMessage(
                role = ChatRole.User,
                content = prompt
            )
            
            val chatCompletionRequest = ChatCompletionRequest(
                model = ModelId("gpt-4o-mini"),
                messages = messages
            )
            
            val completion = openAI.chatCompletion(chatCompletionRequest)
            
            return completion.choices.first().message.content ?: "No se pudo obtener una respuesta."
        } catch (e: Exception) {
            return "Error al comunicarse con la IA: ${e.localizedMessage}"
        }
    }
} 