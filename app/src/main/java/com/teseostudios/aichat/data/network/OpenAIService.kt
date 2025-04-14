package com.teseostudios.aichat.data.network

interface OpenAIService {
    /**
     * Envía un mensaje a la API de OpenAI y obtiene una respuesta.
     * 
     * @param prompt El mensaje a enviar a la API
     * @param previousMessages Lista de mensajes anteriores para dar contexto a la IA
     * @return La respuesta generada por la IA
     */
    suspend fun sendMessage(prompt: String, previousMessages: List<Pair<String, Boolean>>): String
} 