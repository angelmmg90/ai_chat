package com.buildingblocks.android.security.aichat.ui.screens.chat

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.buildingblocks.android.security.aichat.domain.model.Message
import com.buildingblocks.android.security.aichat.ui.theme.AiChatTheme

/**
 * Previews para los componentes de la pantalla de chat
 */
@Preview(showBackground = true)
@Composable
fun ChatScreenPreview() {
    AiChatTheme {
        ChatScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun MessageItemsPreview() {
    AiChatTheme {
        Column {
            MessageItem(
                message = Message(
                    content = "¡Hola! ¿En qué puedo ayudarte hoy?",
                    isFromUser = false
                )
            )
            MessageItem(
                message = Message(
                    content = "Necesito información sobre inteligencia artificial",
                    isFromUser = true
                )
            )
        }
    }
} 