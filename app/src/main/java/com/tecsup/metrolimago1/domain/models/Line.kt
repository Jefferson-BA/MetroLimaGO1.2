package com.tecsup.metrolimago1.domain.models

import com.google.android.gms.maps.model.LatLng

data class Line(
    val id: String,
    val name: String,
    val color: String, // Color hexadecimal
    val status: LineStatus,
    val route: List<LatLng>, // Puntos del recorrido
    val stations: List<Station>, // Estaciones de la línea
    val description: String = "",
    val openingDate: String = "",
    val operatingHours: String = "05:00 - 23:00"
)

enum class LineStatus {
    OPERATIONAL,
    MAINTENANCE,
    EXPANSION,
    CONSTRUCTION
}
