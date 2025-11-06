package com.tecsup.metrolimago1.data.service

import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Servicio de Gemini para MetroLima GO
 * Usa Firebase AI Logic SDK para interactuar con Gemini API
 */
class GeminiService {
    
    // Inicializar el modelo Gemini usando Gemini Developer API (lazy para evitar inicialización temprana)
    private val model by lazy {
        Firebase.ai(backend = GenerativeBackend.googleAI())
            .generativeModel("gemini-2.5-flash")
    }
    
    /**
     * Envía un mensaje a Gemini y obtiene la respuesta
     * 
     * @param message El mensaje del usuario
     * @param conversationHistory Historial de conversación (opcional)
     * @return La respuesta de Gemini
     */
    suspend fun sendMessage(
        message: String,
        conversationHistory: List<Pair<String, String>> = emptyList()
    ): String = withContext(Dispatchers.IO) {
        try {
            // Construir el prompt con contexto del asistente
            val systemPrompt = "Eres un asistente de MetroLima GO. " +
                    "Ayuda a los usuarios con información sobre el metro de Lima, " +
                    "estaciones, rutas, horarios y cualquier pregunta relacionada con el transporte público en Lima. " +
                    "Responde de manera amigable y útil. " +
                    "Si no sabes algo, admítelo honestamente."
            
            // Construir el contenido completo del prompt
            val fullPrompt = buildString {
                append(systemPrompt)
                append("\n\n")
                
                // Agregar historial de conversación si existe
                if (conversationHistory.isNotEmpty()) {
                    append("Historial de conversación:\n")
                    conversationHistory.forEach { (userMsg, assistantMsg) ->
                        append("Usuario: $userMsg\n")
                        append("Asistente: $assistantMsg\n\n")
                    }
                }
                
                append("Usuario: $message\n")
                append("Asistente:")
            }
            
            // Generar contenido usando Gemini
            val response = model.generateContent(fullPrompt)
            
            // Obtener el texto de la respuesta
            response.text ?: "Lo siento, no pude procesar tu mensaje."
            
        } catch (e: Exception) {
            // Manejo de errores
            when {
                e.message?.contains("API key", ignoreCase = true) == true -> 
                    "Error de autenticación. Verifica la configuración de Firebase."
                e.message?.contains("quota", ignoreCase = true) == true || 
                e.message?.contains("rate limit", ignoreCase = true) == true -> 
                    "El servicio está muy ocupado en este momento. Por favor intenta de nuevo en unos minutos."
                e.message?.contains("network", ignoreCase = true) == true || 
                e.message?.contains("connection", ignoreCase = true) == true -> 
                    "Error de conexión. Verifica tu conexión a internet."
                else -> 
                    "Error al procesar tu mensaje: ${e.message ?: "Error desconocido"}"
            }
        }
    }
    
    /**
     * Envía un mensaje con streaming (para respuestas más rápidas)
     * Nota: Esta función puede ser implementada más adelante si se necesita streaming
     */
    suspend fun sendMessageStreaming(
        message: String,
        onChunk: (String) -> Unit
    ) {
        // Implementación futura para streaming
        // Por ahora, usar sendMessage normal
        val response = sendMessage(message)
        onChunk(response)
    }
}

