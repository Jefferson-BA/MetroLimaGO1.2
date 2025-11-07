package com.tecsup.metrolimago1.data.remote

import com.tecsup.metrolimago1.data.remote.api.StationApiService
import com.tecsup.metrolimago1.data.remote.dto.StationDto
import com.tecsup.metrolimago1.domain.models.Station
import com.tecsup.metrolimago1.domain.models.StationStatus
import com.tecsup.metrolimago1.utils.ConfigManager
import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.google.gson.GsonBuilder

/**
 * Servicio para obtener estaciones desde la API de Django
 */
class StationService {
    
    private val retrofit: Retrofit by lazy {
        // Logging interceptor para debug
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        
        // OkHttpClient con interceptor de logging y timeout aumentado
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
            .readTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
            .writeTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
            .build()
        
        // Obtener URL base desde ConfigManager o usar default
        // Si ConfigManager no está inicializado, usar URL de producción en Render
        val baseUrl = try {
            val url = ConfigManager.getMetroLimaBaseUrl()
            // Asegurar que la URL termine con /
            val finalUrl = if (url.endsWith("/")) url else "$url/"
            Log.d("StationService", "Usando URL base de API: $finalUrl")
            finalUrl
        } catch (e: Exception) {
            // Si ConfigManager no está inicializado, usar URL de producción en Render
            val defaultUrl = "https://metrolima-api.onrender.com/api/" // URL de producción en Render
            Log.w("StationService", "ConfigManager no inicializado, usando URL por defecto: $defaultUrl")
            defaultUrl
        }
        
        // Configurar Gson para manejar campos opcionales y nulls correctamente
        val gson = GsonBuilder()
            .serializeNulls() // Incluir campos null en la serialización
            .create()
        
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
    
    private val apiService: StationApiService by lazy {
        retrofit.create(StationApiService::class.java)
    }
    
    /**
     * Obtiene todas las estaciones desde la API
     */
    suspend fun getAllStations(): List<Station> {
        return try {
            Log.d("StationService", "Intentando obtener todas las estaciones...")
            val response = apiService.getAllStations()
            Log.d("StationService", "Respuesta paginada: count=${response.count}, results=${response.results.size}")
            val stations = response.results.map { dto ->
                Log.d("StationService", "Estación DTO: ${dto.name}, imageUrl: ${dto.imageUrl}")
                val station = dto.toDomainModel()
                Log.d("StationService", "Estación convertida: ${station.name}, imageUrl: ${station.imageUrl}")
                station
            }
            stations
        } catch (e: Exception) {
            Log.e("StationService", "Error al obtener estaciones: ${e.message}", e)
            Log.e("StationService", "Tipo de error: ${e.javaClass.simpleName}")
            e.printStackTrace()
            emptyList()
        }
    }
    
    /**
     * Obtiene una estación por ID desde la API
     */
    suspend fun getStationById(id: String): Station? {
        return try {
            Log.d("StationService", "Intentando obtener estación con ID: $id")
            val stationDto = apiService.getStationById(id)
            Log.d("StationService", "Estación obtenida: ${stationDto.name}")
            Log.d("StationService", "imageUrl en DTO: '${stationDto.imageUrl}', isEmpty: ${stationDto.imageUrl?.isEmpty()}")
            // Log del DTO completo para debug
            Log.d("StationService", "DTO completo - id: ${stationDto.id}, name: ${stationDto.name}, line: ${stationDto.line}, imageUrl: '${stationDto.imageUrl}'")
            val station = stationDto.toDomainModel()
            Log.d("StationService", "Estación convertida: ${station.name}, imageUrl: '${station.imageUrl}'")
            station
        } catch (e: Exception) {
            Log.e("StationService", "Error al obtener estación $id: ${e.message}", e)
            Log.e("StationService", "Tipo de error: ${e.javaClass.simpleName}")
            e.printStackTrace()
            null
        }
    }
    
    /**
     * Obtiene estaciones por línea desde la API
     */
    suspend fun getStationsByLine(line: String): List<Station> {
        return try {
            Log.d("StationService", "Obteniendo estaciones de línea: $line")
            val stationsDto = apiService.getStationsByLine(line)
            Log.d("StationService", "Estaciones de línea $line: ${stationsDto.size}")
            stationsDto.map { it.toDomainModel() }
        } catch (e: Exception) {
            Log.e("StationService", "Error al obtener estaciones por línea: ${e.message}", e)
            e.printStackTrace()
            emptyList()
        }
    }
}

/**
 * Extensión para convertir StationDto a Station (modelo de dominio)
 */
private fun StationDto.toDomainModel(): Station {
    return Station(
        id = id,
        name = name,
        line = line,
        address = address,
        latitude = latitude.toDoubleOrNull() ?: 0.0,
        longitude = longitude.toDoubleOrNull() ?: 0.0,
        description = description ?: "",
        openingTime = openingTime,
        closingTime = closingTime,
        status = status.toStationStatus(),
        imageUrl = imageUrl,
        nearbyServices = emptyList() // Los servicios cercanos no vienen de la API por ahora
    )
}

/**
 * Convierte el string de status de la API al enum StationStatus
 */
private fun String.toStationStatus(): StationStatus {
    return when (this.uppercase()) {
        "OPERATIONAL" -> StationStatus.OPERATIONAL
        "MAINTENANCE" -> StationStatus.MAINTENANCE
        "CONSTRUCTION" -> StationStatus.CONSTRUCTION
        "CLOSED" -> StationStatus.CLOSED
        else -> StationStatus.OPERATIONAL
    }
}

