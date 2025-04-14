package com.buildingblocks.android.security.aichat.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.buildingblocks.android.security.aichat.R
import com.buildingblocks.android.security.aichat.domain.model.Conversation

@Composable
fun ConversationDrawer(
    conversations: List<Conversation>,
    selectedConversationId: String?,
    onConversationSelected: (String) -> Unit,
    onDeleteConversation: (String) -> Unit,
    onNewConversation: () -> Unit,
    drawerState: DrawerState,
    modifier: Modifier = Modifier
) {
    ModalDrawerSheet(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Título del drawer
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.title_conversations),
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center
                )
            }
            
            // Botón de Nueva Conversación
            Button(
                onClick = onNewConversation,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add, 
                    contentDescription = stringResource(R.string.content_description_add_icon)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = stringResource(R.string.button_new_conversation))
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            if (conversations.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.text_no_conversations),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            } else {
                // Lista de conversaciones
                LazyColumn {
                    items(conversations) { conversation ->
                        ConversationItem(
                            conversation = conversation,
                            isSelected = conversation.id == selectedConversationId,
                            onConversationClick = onConversationSelected,
                            onDeleteClick = onDeleteConversation
                        )
                    }
                }
            }
        }
    }
} 