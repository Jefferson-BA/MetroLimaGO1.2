package com.tecsup.metrolimago1.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_stations")
data class FavoriteStation(
    @PrimaryKey val id: String,
    val name: String,
    val address: String,
    val line: String,
    val latitude: Double,
    val longitude: Double
)