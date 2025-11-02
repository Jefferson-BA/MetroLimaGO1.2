package com.tecsup.metrolimago1.data.service

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import kotlinx.coroutines.delay

/**
 * Servicio de IA para MetroLima GO
 * Usa OpenAI GPT-3.5-turbo con Retrofit
 */

class AIService {
    
    // API Key de OpenAI
    private val API_KEY = "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"
    
    // URL base de OpenAI API
    private val BASE_URL = "https://api.openai.com/v1/"
    
    /**
     * Envía un mensaje a la IA y obtiene la respuesta
     * 
     * @param message El mensaje del usuario
     * @return La respuesta de la IA
     */
    // Instancia singleton de Retrofit
    private val retrofit: Retrofit by lazy {
        // Interceptor para agregar headers
        val authInterceptor = Interceptor { chain ->
            val originalRequest = chain.request()
            val newRequest = originalRequest.newBuilder()
                .addHeader("Authorization", "Bearer $API_KEY")
                .addHeader("Content-Type", "application/json")
                .build()
            chain.proceed(newRequest)
        }
        
        // Logging interceptor para debug
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        
        // OkHttpClient con interceptores
        val client = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()
        
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    
    private val apiService: AIApiService by lazy {
        retrofit.create(AIApiService::class.java)
    }
    
    suspend fun sendMessage(message: String): String {
        val maxRetries = 3
        var lastException: Exception? = null
        
        for (attempt in 1..maxRetries) {
            try {
                val request = ChatRequest(
                    model = "gpt-3.5-turbo",
                    messages = listOf(
                        Message("system", "Eres un asistente de MetroLima GO. Ayuda a los usuarios con información sobre el metro de Lima."),
                        Message("user", message)
                    ),
                    max_tokens = 500,
                    temperature = 0.7
                )
                
                val response = apiService.sendMessage(request)
                return response.choices.firstOrNull()?.message?.content 
                    ?: "Lo siento, no pude procesar tu mensaje."
                
            } catch (e: Exception) {
                lastException = e
                
                // Si es error 429 (Rate limit), esperar antes de reintentar
                if (e.message?.contains("429") == true || e.message?.contains("rate limit", ignoreCase = true) == true) {
                    if (attempt < maxRetries) {
                        val waitTime = attempt * 2L // 2, 4, 6 segundos
                        delay(waitTime * 1000)
                        continue // Reintentar
                    } else {
                        return "El servicio está muy ocupado en este momento. Por favor intenta de nuevo en unos minutos."
                    }
                }
                
                // Si no es error de rate limit, lanzar excepción
                if (attempt == maxRetries) {
                    break
                }
            }
        }
        
        return when {
            lastException?.message?.contains("429") == true -> 
                "El servicio está muy ocupado. Por favor espera unos segundos e intenta de nuevo."
            lastException?.message?.contains("API key") == true -> 
                "Error de autenticación. Verifica tu API key."
            else -> 
                "Error al procesar tu mensaje: ${lastException?.message ?: "Error desconocido"}"
        }
    }
    
}

// Definir la interfaz para Retrofit
interface AIApiService {
    @POST("chat/completions")
    suspend fun sendMessage(
        @Body request: ChatRequest
    ): ChatResponse
}

// Modelos de datos para OpenAI API
data class ChatRequest(
    val model: String = "gpt-3.5-turbo",
    val messages: List<Message>,
    val max_tokens: Int = 500,
    val temperature: Double = 0.7
)

data class Message(
    val role: String,
    val content: String
)

data class ChatResponse(
    val choices: List<Choice>
)

data class Choice(
    val message: Message
)

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
