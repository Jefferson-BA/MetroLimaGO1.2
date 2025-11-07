package com.tecsup.metrolimago1.domain.models

data class Station(
    val id: String,
    val name: String,
    val line: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val description: String,
    val openingTime: String = "05:00",
    val closingTime: String = "23:00",
    val status: StationStatus = StationStatus.OPERATIONAL,
    val imageUrl: String? = "",
    val nearbyServices: List<NearbyService> = emptyList()
)

enum class StationStatus {
    OPERATIONAL,
    MAINTENANCE,
    CONSTRUCTION,
    CLOSED
}

data class NearbyService(
    val type: ServiceType,
    val name: String,
    val address: String,
    val distance: String
)

enum class ServiceType {
    RESTAURANT,
    BANK,
    PHARMACY,
    HOSPITAL,
    UNIVERSITY,
    MALL,
    PARK,
    ATM
}
