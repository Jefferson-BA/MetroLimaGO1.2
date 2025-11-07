package com.tecsup.metrolimago1.data.remote.api

import com.tecsup.metrolimago1.data.remote.dto.PaginatedResponse
import com.tecsup.metrolimago1.data.remote.dto.StationDto
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Interfaz de Retrofit para la API de estaciones de Django
 */
interface StationApiService {
    /**
     * Obtiene todas las estaciones (respuesta paginada de Django REST Framework)
     */
    @GET("stations/")
    suspend fun getAllStations(): PaginatedResponse<StationDto>

    /**
     * Obtiene una estación por ID
     */
    @GET("stations/{id}/")
    suspend fun getStationById(@Path("id") id: String): StationDto

    /**
     * Obtiene estaciones por línea (lista directa, no paginada)
     */
    @GET("stations/by_line/")
    suspend fun getStationsByLine(@retrofit2.http.Query("line") line: String): List<StationDto>
}

