package com.buildingblocks.android.security.aichat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.buildingblocks.android.security.aichat.ui.screens.chat.ChatScreen
import com.buildingblocks.android.security.aichat.ui.theme.AiChatTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AiChatTheme {
                ChatScreen()
            }
        }
    }
}