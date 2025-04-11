package com.buildingblocks.android.security.aichat.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.buildingblocks.android.security.aichat.domain.model.Message
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor() : ViewModel() {
    
    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()
    
    fun sendMessage(content: String) {
        if (content.isBlank()) return
        
        val userMessage = Message(
            content = content,
            isFromUser = true
        )
        
        _uiState.update { currentState ->
            currentState.copy(
                messages = currentState.messages + userMessage
            )
        }
        
        // Simulate AI response (will be replaced with actual AI call later)
        viewModelScope.launch {
            val aiResponse = Message(
                content = "Estoy procesando tu mensaje: \"$content\"",
                isFromUser = false
            )
            
            _uiState.update { currentState ->
                currentState.copy(
                    messages = currentState.messages + aiResponse
                )
            }
        }
    }
}

data class ChatUiState(
    val messages: List<Message> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
) 