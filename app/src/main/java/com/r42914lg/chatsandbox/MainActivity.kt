package com.r42914lg.chatsandbox

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import com.r42914lg.chatsandbox.conversation.ConversationContent
import com.r42914lg.chatsandbox.conversation.ConversationUiState
import com.r42914lg.chatsandbox.conversation.initialMessages
import com.r42914lg.chatsandbox.ui.theme.ChatSandboxTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChatSandboxTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    ConversationContent(uiState = ConversationUiState(initialMessages))
                }
            }
        }
    }
}