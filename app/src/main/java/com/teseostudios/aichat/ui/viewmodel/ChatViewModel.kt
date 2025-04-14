package com.teseostudios.aichat.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teseostudios.aichat.R
import com.teseostudios.aichat.data.repository.ChatOperationResult
import com.teseostudios.aichat.data.repository.ChatRepository
import com.teseostudios.aichat.domain.model.Message
import com.teseostudios.aichat.domain.repository.ConversationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    private val conversationRepository: ConversationRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()
    
    private val _currentConversationId = MutableStateFlow<String?>(null)
    
    val conversations = conversationRepository.getAllConversations()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    
    // Obtener los mensajes de la conversación actual
    @OptIn(ExperimentalCoroutinesApi::class)
    private val currentMessages = _currentConversationId
        .filterNotNull()
        .flatMapLatest { conversationId ->
            conversationRepository.getMessagesForConversation(conversationId)
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    
    // Combinar el estado actual con los mensajes para tener un estado único
    init {
        viewModelScope.launch {
            currentMessages.collectLatest { messages ->
                _uiState.update { currentState ->
                    currentState.copy(messages = messages)
                }
            }
        }
    }
    
    fun createNewConversation() {
        viewModelScope.launch {
            val defaultTitle = context.getString(R.string.default_conversation_title)
            val conversationId = conversationRepository.createConversation(defaultTitle)
            _currentConversationId.value = conversationId
            _uiState.update { it.copy(messages = emptyList()) }
        }
    }
    
    fun selectConversation(conversationId: String) {
        _currentConversationId.value = conversationId
    }
    
    fun getCurrentConversationId(): String? {
        return _currentConversationId.value
    }
    
    fun deleteConversation(conversationId: String) {
        viewModelScope.launch {
            // Eliminar la conversación
            conversationRepository.deleteConversation(conversationId)
            
            // Si la conversación eliminada es la que estaba activa,
            // seleccionar otra conversación
            if (conversationId == _currentConversationId.value) {
                // Obtener la lista actual de conversaciones
                val currentConversations = conversations.value
                
                if (currentConversations.isEmpty()) {
                    // Si no hay más conversaciones, crear una nueva
                    createNewConversation()
                } else {
                    // Seleccionar la primera conversación disponible
                    selectConversation(currentConversations.first().id)
                }
            }
        }
    }
    
    fun sendMessage(content: String) {
        if (content.isBlank()) return
        
        // Si no hay conversación activa, crear una nueva
        if (_currentConversationId.value == null) {
            viewModelScope.launch {
                val defaultTitle = context.getString(R.string.default_conversation_title)
                val conversationId = conversationRepository.createConversation(defaultTitle)
                _currentConversationId.value = conversationId
                sendMessageToConversation(content, conversationId)
            }
        } else {
            sendMessageToConversation(content, _currentConversationId.value!!)
        }
    }
    
    private fun sendMessageToConversation(content: String, conversationId: String) {
        val userMessage = Message(
            content = content,
            isFromUser = true
        )
        
        viewModelScope.launch {
            // Guardar el mensaje del usuario en la conversación
            conversationRepository.addMessageToConversation(conversationId, userMessage)
            
            // Actualizar el título de la conversación si es el primer mensaje
            val messages = currentMessages.value
            if (messages.size <= 1) {
                generateConversationTitle(content, conversationId)
            }
            
            // Enviar mensaje a la IA
            chatRepository.sendMessage(content, currentMessages.value).collect { result ->
                when (result) {
                    is ChatOperationResult.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                    is ChatOperationResult.Success -> {
                        // Guardar la respuesta en la conversación
                        conversationRepository.addMessageToConversation(conversationId, result.message)
                        _uiState.update { it.copy(isLoading = false, error = null) }
                    }
                    is ChatOperationResult.Error -> {
                        _uiState.update { it.copy(
                            isLoading = false,
                            error = result.message
                        ) }
                        
                        val errorMessage = Message(
                            content = context.getString(R.string.error_message_template, result.message),
                            isFromUser = false
                        )
                        
                        // Guardar el mensaje de error en la conversación
                        conversationRepository.addMessageToConversation(conversationId, errorMessage)
                    }
                }
            }
        }
    }
    
    private fun generateConversationTitle(firstMessage: String, conversationId: String) {
        viewModelScope.launch {
            try {
                // Crear un mensaje específico para generar el título
                val prompt = context.getString(R.string.prompt_generate_title, firstMessage)
                
                chatRepository.sendMessage(prompt, emptyList()).collect { result ->
                    if (result is ChatOperationResult.Success) {
                        // Extraer el título de la respuesta
                        val title = result.message.content.trim()
                        // Actualizar el título de la conversación
                        conversationRepository.updateConversationTitle(conversationId, title)
                    }
                }
            } catch (e: Exception) {
                // Si falla, utilizamos el mensaje original truncado
                val fallbackTitle = if (firstMessage.length > 20) 
                    "${firstMessage.take(20)}..." 
                else 
                    firstMessage
                    
                conversationRepository.updateConversationTitle(conversationId, fallbackTitle)
            }
        }
    }
}

data class ChatUiState(
    val messages: List<Message> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
) 