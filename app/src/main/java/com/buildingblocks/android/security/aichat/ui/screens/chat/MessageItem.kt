package com.buildingblocks.android.security.aichat.ui.screens.chat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.buildingblocks.android.security.aichat.domain.model.Message

/**
 * Componente que muestra un mensaje individual en la lista de mensajes
 */
@Composable
fun MessageItem(message: Message, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = if (message.isFromUser) Arrangement.End else Arrangement.Start
    ) {
        // Limitamos el ancho máximo de las burbujas de chat al 80% de la pantalla
        Box(
            modifier = Modifier
                .widthIn(max = 300.dp)
                .padding(4.dp)
        ) {
            Card(
                shape = getMessageShape(message.isFromUser),
                colors = CardDefaults.cardColors(
                    containerColor = getMessageBackgroundColor(message.isFromUser)
                )
            ) {
                Text(
                    text = message.content,
                    modifier = Modifier.padding(12.dp),
                    color = getMessageTextColor(message.isFromUser),
                    fontWeight = if (message.isFromUser) FontWeight.Normal else FontWeight.Medium
                )
            }
        }
    }
}

/**
 * Determina la forma de la burbuja de chat según el remitente
 */
@Composable
private fun getMessageShape(isFromUser: Boolean): RoundedCornerShape {
    return RoundedCornerShape(
        topStart = 16.dp,
        topEnd = 16.dp,
        bottomStart = if (isFromUser) 16.dp else 4.dp,
        bottomEnd = if (isFromUser) 4.dp else 16.dp
    )
}

/**
 * Determina el color de fondo de la burbuja de chat según el remitente
 */
@Composable
private fun getMessageBackgroundColor(isFromUser: Boolean) = if (isFromUser) {
    MaterialTheme.colorScheme.primary
} else {
    MaterialTheme.colorScheme.surfaceVariant
}

/**
 * Determina el color del texto según el remitente
 */
@Composable
private fun getMessageTextColor(isFromUser: Boolean) = if (isFromUser) {
    MaterialTheme.colorScheme.onPrimary
} else {
    MaterialTheme.colorScheme.onSurfaceVariant
} 