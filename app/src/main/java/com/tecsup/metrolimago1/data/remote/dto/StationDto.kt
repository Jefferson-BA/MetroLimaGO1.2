package com.tecsup.metrolimago1.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * DTO para la respuesta de la API de Django
 * Representa una estación tal como viene del servidor
 */
data class StationDto(
    val id: String,
    val name: String,
    val line: String,
    val address: String,
    val latitude: String, // La API devuelve String
    val longitude: String, // La API devuelve String
    val description: String? = "",
    @SerializedName("opening_time")
    val openingTime: String = "05:00",
    @SerializedName("closing_time")
    val closingTime: String = "23:00",
    val status: String, // "OPERATIONAL", "MAINTENANCE", etc.
    @SerializedName("image_url")
    val imageUrl: String = ""
)

