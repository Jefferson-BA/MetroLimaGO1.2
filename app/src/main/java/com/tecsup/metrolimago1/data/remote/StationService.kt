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

/**
 * Servicio para obtener estaciones desde la API de Django
 */
class StationService {
    
    private val retrofit: Retrofit by lazy {
        // Logging interceptor para debug
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        
        // OkHttpClient con interceptor de logging
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
        
        // Obtener URL base desde ConfigManager o usar default
        // Si ConfigManager no está inicializado, usar localhost por defecto
        val baseUrl = try {
            val url = ConfigManager.getMetroLimaBaseUrl()
            // Asegurar que la URL termine con /
            val finalUrl = if (url.endsWith("/")) url else "$url/"
            Log.d("StationService", "Usando URL base de API: $finalUrl")
            finalUrl
        } catch (e: Exception) {
            // Si ConfigManager no está inicializado, usar localhost por defecto
            // IMPORTANTE: Cambia esta IP por la IP de tu máquina donde corre Django
            // En Windows: ipconfig en CMD para ver tu IPv4
            // En Linux/Mac: ifconfig o ip addr
            
            // IMPORTANTE: Cambia esta URL según tu caso:
            // - Si usas EMULADOR Android: usa "http://10.0.2.2:8000/api/"
            // - Si usas DISPOSITIVO FÍSICO: usa la IP de tu máquina "http://172.20.10.4:8000/api/"
            // 
            // Para encontrar tu IP: ipconfig (Windows) o ifconfig (Linux/Mac)
            // Recompila la app después de cambiar esta URL
            
            val defaultUrl = "http://10.0.2.2:8000/api/" // Para EMULADOR
            // val defaultUrl = "http://172.20.10.4:8000/api/" // Para DISPOSITIVO FÍSICO - Descomenta esta línea y comenta la anterior
            Log.w("StationService", "ConfigManager no inicializado, usando URL por defecto: $defaultUrl")
            defaultUrl
        }
        
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
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
            val stationsDto = apiService.getAllStations()
            stationsDto.map { it.toDomainModel() }
        } catch (e: Exception) {
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
            Log.d("StationService", "Estación obtenida: ${stationDto.name}, imageUrl: ${stationDto.imageUrl}")
            val station = stationDto.toDomainModel()
            Log.d("StationService", "Estación convertida: ${station.name}, imageUrl: ${station.imageUrl}")
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
            val stationsDto = apiService.getStationsByLine(line)
            stationsDto.map { it.toDomainModel() }
        } catch (e: Exception) {
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
        description = description,
        openingTime = openingTime,
        closingTime = closingTime,
        status = status.toStationStatus(),
        imageUrl = imageUrl ?: "",
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

