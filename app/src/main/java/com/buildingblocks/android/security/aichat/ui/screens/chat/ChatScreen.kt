package com.buildingblocks.android.security.aichat.ui.screens.chat

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.buildingblocks.android.security.aichat.ui.components.ConversationDrawer
import com.buildingblocks.android.security.aichat.ui.components.TopBar
import com.buildingblocks.android.security.aichat.ui.viewmodel.ChatViewModel
import kotlinx.coroutines.launch

/**
 * Pantalla principal del chat
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    viewModel: ChatViewModel = hiltViewModel()
) {
    val chatUiState by viewModel.uiState.collectAsState()
    val conversations by viewModel.conversations.collectAsState()
    val currentConversationId = viewModel.getCurrentConversationId()
    
    var messageText by remember { mutableStateOf(TextFieldValue("")) }
    val listState = rememberLazyListState()
    val snackbarHostState = remember { SnackbarHostState() }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    
    // Mostrar mensajes de error en Snackbar
    chatUiState.error?.let { error ->
        LaunchedEffect(error) {
            snackbarHostState.showSnackbar(
                message = error
            )
        }
    }
    
    // Scroll automático al último mensaje cuando se añade uno nuevo
    LaunchedEffect(chatUiState.messages.size) {
        if (chatUiState.messages.isNotEmpty()) {
            listState.animateScrollToItem(chatUiState.messages.size - 1)
        }
    }
    
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ConversationDrawer(
                conversations = conversations,
                selectedConversationId = currentConversationId,
                onConversationSelected = { conversationId ->
                    viewModel.selectConversation(conversationId)
                    scope.launch {
                        drawerState.close()
                    }
                },
                onDeleteConversation = { conversationId ->
                    viewModel.deleteConversation(conversationId)
                },
                onNewConversation = {
                    viewModel.createNewConversation()
                    scope.launch {
                        drawerState.close()
                    }
                },
                drawerState = drawerState
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopBar(
                    title = "AI Chat",
                    onMenuClick = {
                        scope.launch {
                            drawerState.open()
                        }
                    }
                )
            },
            snackbarHost = { SnackbarHost(snackbarHostState) },
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
                    listState = listState,
                    isLoading = chatUiState.isLoading
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
} 