package com.buildingblocks.android.security.aichat.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.buildingblocks.android.security.aichat.data.repository.ChatOperationResult
import com.buildingblocks.android.security.aichat.data.repository.ChatRepository
import com.buildingblocks.android.security.aichat.domain.model.Conversation
import com.buildingblocks.android.security.aichat.domain.model.Message
import com.buildingblocks.android.security.aichat.domain.repository.ConversationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
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
    private val conversationRepository: ConversationRepository
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
            val conversationId = conversationRepository.createConversation("Nueva conversación")
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
    
    fun sendMessage(content: String) {
        if (content.isBlank()) return
        
        // Si no hay conversación activa, crear una nueva
        if (_currentConversationId.value == null) {
            viewModelScope.launch {
                val conversationId = conversationRepository.createConversation(content)
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
                conversationRepository.updateConversationTitle(conversationId, content)
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
                            content = "Lo siento, ha ocurrido un error: ${result.message}",
                            isFromUser = false
                        )
                        
                        // Guardar el mensaje de error en la conversación
                        conversationRepository.addMessageToConversation(conversationId, errorMessage)
                    }
                }
            }
        }
    }
}

data class ChatUiState(
    val messages: List<Message> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
) 