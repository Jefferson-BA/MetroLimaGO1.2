package com.tecsup.metrolimago1.ui.screens.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.tecsup.metrolimago1.R
import com.tecsup.metrolimago1.ui.theme.*
import com.tecsup.metrolimago1.ui.theme.LocalThemeState
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.tecsup.metrolimago1.ui.theme.MetroLimaGO1Theme
import com.tecsup.metrolimago1.ui.theme.ThemeState
import androidx.compose.runtime.CompositionLocalProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(navController: NavController) {
    val themeState = LocalThemeState.current

    // Colores dinámicos según el tema
    val cardColor = if (themeState.isDarkMode) CardGray else Color(0xFFFFFFFF)
    val textColor = if (themeState.isDarkMode) White else Color(0xFF1C1C1C)
    val secondaryTextColor = if (themeState.isDarkMode) LightGray else Color(0xFF666666)

    // Estado del chat
    var messageText by remember { mutableStateOf("") }
    var messages by remember { mutableStateOf(listOf<ChatMessage>()) }
    var isLoading by remember { mutableStateOf(false) }

    // Mensaje de bienvenida inicial
    LaunchedEffect(Unit) {
        if (messages.isEmpty()) {
            messages = listOf(
                ChatMessage(
                    text = "¡Hola! Soy tu asistente de MetroLima GO. ¿En qué puedo ayudarte hoy?",
                    isUser = false,
                    timestamp = System.currentTimeMillis()
                )
            )
        }
    }

    // Simular respuesta IA
    LaunchedEffect(isLoading) {
        if (isLoading) {
            kotlinx.coroutines.delay(1500)
            val aiResponse = ChatMessage(
                text = "Entiendo tu consulta. Como asistente de MetroLima GO, puedo ayudarte con información sobre estaciones, horarios, rutas y más. ¿Qué necesitas saber específicamente?",
                isUser = false,
                timestamp = System.currentTimeMillis()
            )
            messages = messages + aiResponse
            isLoading = false
        }
    }

    GradientBackground(isDarkMode = themeState.isDarkMode) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = painterResource(id = R.drawable.ia),
                                contentDescription = "Robot IA",
                                modifier = Modifier.size(50.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Asistente IA",
                                color = textColor
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Volver",
                                tint = textColor
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = cardColor
                    )
                )
            },
            bottomBar = {},
            containerColor = Color.Transparent,
            modifier = Modifier.fillMaxSize()
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = paddingValues.calculateTopPadding(),
                        start = 0.dp,
                        end = 0.dp,
                        bottom = 0.dp
                    )
            ) {
                // Lista de mensajes
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    reverseLayout = false
                ) {
                    items(messages) { message ->
                        ChatMessageBubble(
                            message = message,
                            textColor = textColor,
                            cardColor = cardColor,
                            secondaryTextColor = secondaryTextColor
                        )
                    }

                    if (isLoading) {
                        item {
                            TypingIndicator(
                                cardColor = cardColor,
                                textColor = secondaryTextColor
                            )
                        }
                    }
                }

                // Barra de entrada de mensaje
                MessageInputBar(
                    messageText = messageText,
                    onMessageTextChange = { messageText = it },
                    onSendMessage = {
                        if (messageText.isNotBlank()) {
                            val userMessage = ChatMessage(
                                text = messageText,
                                isUser = true,
                                timestamp = System.currentTimeMillis()
                            )
                            messages = messages + userMessage
                            isLoading = true
                            messageText = ""
                        }
                    },
                    cardColor = cardColor,
                    textColor = textColor,
                    secondaryTextColor = secondaryTextColor
                )
            }
        }
    }
}

@Composable
fun ChatMessageBubble(
    message: ChatMessage,
    textColor: Color,
    cardColor: Color,
    secondaryTextColor: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (message.isUser) Arrangement.End else Arrangement.Start
    ) {
        Card(
            modifier = Modifier.widthIn(max = 280.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (message.isUser) MetroOrange else cardColor
            ),
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (message.isUser) 16.dp else 4.dp,
                bottomEnd = if (message.isUser) 4.dp else 16.dp
            )
        ) {
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Text(
                    text = message.text,
                    color = if (message.isUser) White else textColor,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = formatTime(message.timestamp),
                    color = if (message.isUser) White.copy(alpha = 0.7f) else secondaryTextColor,
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp)
                )
            }
        }
    }
}

@Composable
fun MessageInputBar(
    messageText: String,
    onMessageTextChange: (String) -> Unit,
    onSendMessage: () -> Unit,
    cardColor: Color,
    textColor: Color,
    secondaryTextColor: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 35.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        shape = RoundedCornerShape(38.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = messageText,
                onValueChange = onMessageTextChange,
                placeholder = {
                    Text(
                        text = "Escribe tu mensaje...",
                        color = secondaryTextColor
                    )
                },
                modifier = Modifier
                    .weight(1f)
                    .offset(x = (-5).dp),
                shape = RoundedCornerShape(28.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = textColor,
                    unfocusedTextColor = textColor,
                    focusedBorderColor = MetroOrange,
                    unfocusedBorderColor = secondaryTextColor
                ),
                textStyle = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(
                onClick = { onSendMessage() },
                modifier = Modifier
                    .size(44.dp)
                    .background(
                        MetroOrange,
                        RoundedCornerShape(28.dp)
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Enviar",
                    tint = White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun TypingIndicator(
    cardColor: Color,
    textColor: Color
) {
    Card(
        modifier = Modifier.widthIn(max = 120.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        shape = RoundedCornerShape(28.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Escribiendo",
                color = textColor,
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.width(4.dp))
            CircularProgressIndicator(
                modifier = Modifier.size(12.dp),
                color = MetroOrange,
                strokeWidth = 2.dp
            )
        }
    }
}

data class ChatMessage(
    val text: String,
    val isUser: Boolean,
    val timestamp: Long
)

fun formatTime(timestamp: Long): String {
    val time = java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault())
    return time.format(java.util.Date(timestamp))
}

@Preview(showBackground = true, showSystemUi = true, name = "💡 ChatScreen - Modo Claro")
@Composable
fun ChatScreenPreviewLight() {
    val navController = rememberNavController()
    val themeState = remember { ThemeState() }.apply { updateDarkMode(false) }

    CompositionLocalProvider(LocalThemeState provides themeState) {
        MetroLimaGO1Theme(darkTheme = themeState.isDarkMode) {
            ChatScreen(navController = navController)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "🌙 ChatScreen - Modo Oscuro")
@Composable
fun ChatScreenPreviewDark() {
    val navController = rememberNavController()
    val themeState = remember { ThemeState() }.apply { updateDarkMode(true) }

    CompositionLocalProvider(LocalThemeState provides themeState) {
        MetroLimaGO1Theme(darkTheme = themeState.isDarkMode) {
            ChatScreen(navController = navController)
        }
    }
}
