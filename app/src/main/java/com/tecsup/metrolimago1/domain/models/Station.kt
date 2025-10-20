package com.tecsup.metrolimago1.domain.models

data class Station(
    val id: String,
    val name: String,
    val line: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val description: String
)
