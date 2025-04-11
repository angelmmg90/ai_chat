package com.buildingblocks.android.security.aichat.ui.screens.chat

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.buildingblocks.android.security.aichat.ui.viewmodel.ChatViewModel

/**
 * Pantalla principal del chat
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    viewModel: ChatViewModel = hiltViewModel()
) {
    val chatUiState by viewModel.uiState.collectAsState()
    var messageText by remember { mutableStateOf(TextFieldValue("")) }
    val listState = rememberLazyListState()
    
    // Scroll automático al último mensaje cuando se añade uno nuevo
    LaunchedEffect(chatUiState.messages.size) {
        if (chatUiState.messages.isNotEmpty()) {
            listState.animateScrollToItem(chatUiState.messages.size - 1)
        }
    }
    
    Scaffold(
        topBar = {
            ChatTopBar()
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Lista de mensajes
            MessagesList(
                messages = chatUiState.messages,
                listState = listState
            )
            
            // Entrada de mensajes en la parte inferior
            MessageInput(
                messageText = messageText,
                onMessageChange = { messageText = it },
                onSendMessage = {
                    viewModel.sendMessage(messageText.text)
                    messageText = TextFieldValue("")
                },
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}

/**
 * Barra superior del chat
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChatTopBar() {
    TopAppBar(
        title = { Text("AI Chat") },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    )
} 