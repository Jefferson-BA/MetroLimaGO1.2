package com.tecsup.metrolimago1.data.service

import kotlinx.coroutines.delay

/**
 * Servicio de IA para MetroLima GO
 * 
 * AQUÍ DEBES INTEGRAR TU API DE IA:
 * 
 * 1. Agrega tu token/API key en las constantes
 * 2. Implementa la función sendMessage() con tu API
 * 3. Maneja los errores apropiadamente
 * 
 * Ejemplos de APIs que puedes usar:
 * - OpenAI GPT
 * - Google Gemini
 * - Claude AI
 * - Azure OpenAI
 * - O cualquier otra API de IA
 */

class AIService {
    
    // TODO: Reemplaza con tu token/API key real
    private val API_KEY = "TU_API_KEY_AQUI"
    private val API_URL = "https://api.tu-servicio-ia.com/v1/chat/completions"
    
    /**
     * Envía un mensaje a la IA y obtiene la respuesta
     * 
     * @param message El mensaje del usuario
     * @return La respuesta de la IA
     */
    suspend fun sendMessage(message: String): String {
        return try {
            // TODO: Implementa aquí la llamada a tu API de IA
            // Ejemplo de implementación:
            
            /*
            val client = HttpClient {
                install(ContentNegotiation) {
                    json()
                }
            }
            
            val requestBody = mapOf(
                "model" to "gpt-3.5-turbo", // o tu modelo preferido
                "messages" to listOf(
                    mapOf("role" to "system", "content" to "Eres un asistente de MetroLima GO. Ayuda a los usuarios con información sobre el metro de Lima."),
                    mapOf("role" to "user", "content" to message)
                ),
                "max_tokens" to 500,
                "temperature" to 0.7
            )
            
            val response = client.post(API_URL) {
                headers {
                    append("Authorization", "Bearer $API_KEY")
                    append("Content-Type", "application/json")
                }
                setBody(requestBody)
            }
            
            val responseBody = response.body<Map<String, Any>>()
            responseBody["choices"]?.let { choices ->
                if (choices is List<*> && choices.isNotEmpty()) {
                    val firstChoice = choices[0] as? Map<String, Any>
                    val messageContent = firstChoice?.get("message") as? Map<String, Any>
                    messageContent?.get("content") as? String ?: "Lo siento, no pude procesar tu mensaje."
                } else {
                    "Lo siento, no pude procesar tu mensaje."
                }
            } ?: "Lo siento, no pude procesar tu mensaje."
            */
            
            // Respuesta simulada por ahora
            simulateAIResponse(message)
            
        } catch (e: Exception) {
            "Lo siento, hubo un error al procesar tu mensaje. Error: ${e.message}"
        }
    }
    
    /**
     * Simula una respuesta de IA (reemplaza esto con tu implementación real)
     */
    private suspend fun simulateAIResponse(message: String): String {
        delay(1000) // Simula tiempo de procesamiento
        
        return when {
            message.contains("estación", ignoreCase = true) -> {
                "Puedo ayudarte con información sobre las estaciones del Metro de Lima. ¿Qué estación específica te interesa?"
            }
            message.contains("horario", ignoreCase = true) -> {
                "Los horarios del Metro de Lima son de 5:00 AM a 11:00 PM de lunes a domingo. ¿Necesitas información sobre alguna línea específica?"
            }
            message.contains("ruta", ignoreCase = true) -> {
                "Te puedo ayudar a planificar tu ruta. ¿Desde qué estación quieres partir y a cuál quieres llegar?"
            }
            message.contains("precio", ignoreCase = true) || message.contains("costo", ignoreCase = true) -> {
                "El precio del pasaje del Metro de Lima es de S/ 2.50 para adultos y S/ 1.25 para estudiantes con carné."
            }
            else -> {
                "Como asistente de MetroLima GO, puedo ayudarte con información sobre estaciones, horarios, rutas, precios y más. ¿En qué más puedo asistirte?"
            }
        }
    }
}

/**
 * Ejemplo de uso en ChatScreen:
 * 
 * val aiService = AIService()
 * 
 * LaunchedEffect(messageText) {
 *     try {
 *         val response = aiService.sendMessage(messageText)
 *         val aiMessage = ChatMessage(
 *             text = response,
 *             isUser = false,
 *             timestamp = System.currentTimeMillis()
 *         )
 *         messages = messages + aiMessage
 *     } catch (e: Exception) {
 *         val errorMessage = ChatMessage(
 *             text = "Lo siento, hubo un error al procesar tu mensaje.",
 *             isUser = false,
 *             timestamp = System.currentTimeMillis()
 *         )
 *         messages = messages + errorMessage
 *     } finally {
 *         isLoading = false
 *     }
 * }
 */
