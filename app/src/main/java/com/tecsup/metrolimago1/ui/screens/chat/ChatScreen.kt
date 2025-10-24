package com.tecsup.metrolimago1.ui.screens.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import com.tecsup.metrolimago1.utils.TranslationUtils
import com.tecsup.metrolimago1.utils.LocaleUtils
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.tecsup.metrolimago1.ui.theme.MetroLimaGO1Theme
import com.tecsup.metrolimago1.ui.theme.ThemeState
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.text.font.FontWeight

// Sistema de respuestas predefinidas
object ChatResponses {
    private val responses = mapOf(
        "hola" to mapOf(
            "es" to "¡Hola! Soy tu asistente de MetroLima GO. ¿En qué puedo ayudarte?",
            "en" to "Hello! I'm your MetroLima GO assistant. How can I help you?"
        ),
        "horarios" to mapOf(
            "es" to "Los horarios del Metro de Lima son:\n• Lunes a Viernes: 5:30 AM - 11:00 PM\n• Sábados: 5:30 AM - 11:00 PM\n• Domingos: 6:00 AM - 11:00 PM",
            "en" to "Metro de Lima operating hours:\n• Monday to Friday: 5:30 AM - 11:00 PM\n• Saturday: 5:30 AM - 11:00 PM\n• Sunday: 6:00 AM - 11:00 PM"
        ),
        "precio" to mapOf(
            "es" to "El precio del pasaje es:\n• Adultos: S/ 1.50\n• Estudiantes: S/ 0.75\n• Adultos mayores: S/ 0.75",
            "en" to "Ticket prices are:\n• Adults: S/ 1.50\n• Students: S/ 0.75\n• Senior citizens: S/ 0.75"
        ),
        "estaciones" to mapOf(
            "es" to "Las estaciones disponibles son:\n• Línea 1: Villa El Salvador, Pumacahua, San Juan, Atocongo, etc.\n• Línea 2: Ate, Santa Anita, El Agustino, etc.\n• Línea 3: En construcción",
            "en" to "Available stations are:\n• Line 1: Villa El Salvador, Pumacahua, San Juan, Atocongo, etc.\n• Line 2: Ate, Santa Anita, El Agustino, etc.\n• Line 3: Under construction"
        ),
        "ruta" to mapOf(
            "es" to "Para planificar tu ruta:\n1. Ve a la sección 'Rutas'\n2. Selecciona tu estación de origen\n3. Selecciona tu destino\n4. El sistema te mostrará la mejor ruta",
            "en" to "To plan your route:\n1. Go to the 'Routes' section\n2. Select your origin station\n3. Select your destination\n4. The system will show you the best route"
        ),
        "ayuda" to mapOf(
            "es" to "Puedo ayudarte con:\n• Horarios del metro\n• Precios de pasajes\n• Estaciones disponibles\n• Planificación de rutas\n• Estado del servicio",
            "en" to "I can help you with:\n• Metro schedules\n• Ticket prices\n• Available stations\n• Route planning\n• Service status"
        ),
        "contacto" to mapOf(
            "es" to "Para contacto directo:\n• Teléfono: (01) 204-0000\n• Email: info@metrolima.com\n• Web: www.metrolima.com",
            "en" to "For direct contact:\n• Phone: (01) 204-0000\n• Email: info@metrolima.com\n• Web: www.metrolima.com"
        )
    )
    
    fun getResponse(input: String, language: String): String {
        val normalizedInput = input.lowercase().trim()
        
        // Buscar coincidencias exactas
        responses.forEach { (key, translations) ->
            if (normalizedInput.contains(key)) {
                return translations[language] ?: translations["es"] ?: "Lo siento, no entiendo tu pregunta."
            }
        }
        
        // Respuesta por defecto
        return when (language) {
            "es" -> "Lo siento, no entiendo tu pregunta. Puedes preguntarme sobre horarios, precios, estaciones o rutas."
            else -> "Sorry, I don't understand your question. You can ask me about schedules, prices, stations or routes."
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(navController: NavController) {
    val themeState = LocalThemeState.current
    val context = LocalContext.current

    // Colores dinámicos según el tema
    val cardColor = if (themeState.isDarkMode) CardGray else LightCard
    val textColor = if (themeState.isDarkMode) White else LightTextPrimary
    val secondaryTextColor = if (themeState.isDarkMode) LightGray else LightTextSecondary

    // Estado del chat
    var messageText by remember { mutableStateOf("") }
    var messages by remember { mutableStateOf(listOf<ChatMessage>()) }
    var isLoading by remember { mutableStateOf(false) }

    // Mensaje de bienvenida inicial
    LaunchedEffect(Unit) {
        if (messages.isEmpty()) {
            val currentLanguage = context.resources.configuration.locales[0].language
            val welcomeMessage = if (currentLanguage == "es") {
                "¡Hola! Soy tu asistente de MetroLima GO. ¿En qué puedo ayudarte hoy?"
            } else {
                "Hello! I'm your MetroLima GO assistant. How can I help you today?"
            }
            
            messages = listOf(
                ChatMessage(
                    text = welcomeMessage,
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
            
            val currentLanguage = context.resources.configuration.locales[0].language
            val aiResponse = ChatResponses.getResponse(messageText, currentLanguage)
            
            messages = messages + ChatMessage(
                text = aiResponse,
                isUser = false,
                timestamp = System.currentTimeMillis()
            )
            
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
                                Icons.Default.ArrowBack,
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
                    Icons.Default.Send,
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
