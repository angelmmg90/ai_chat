package com.buildingblocks.android.security.aichat.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.buildingblocks.android.security.aichat.data.repository.ChatOperationResult
import com.buildingblocks.android.security.aichat.data.repository.ChatRepository
import com.buildingblocks.android.security.aichat.domain.model.Message
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository
) : ViewModel() {
    
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
        
        viewModelScope.launch {
            chatRepository.sendMessage(content, _uiState.value.messages).collect { result ->
                when (result) {
                    is ChatOperationResult.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                    is ChatOperationResult.Success -> {
                        _uiState.update { currentState ->
                            currentState.copy(
                                messages = currentState.messages + result.message,
                                isLoading = false,
                                error = null
                            )
                        }
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
                        
                        _uiState.update { currentState ->
                            currentState.copy(
                                messages = currentState.messages + errorMessage
                            )
                        }
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