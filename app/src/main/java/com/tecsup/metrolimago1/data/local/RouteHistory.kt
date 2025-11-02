package com.tecsup.metrolimago1.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "route_history")
data class RouteHistory(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val originName: String,
    val originId: String,
    val destinationName: String,
    val destinationId: String,
    val distance: String,
    val duration: String,
    val routeType: String, // "fastest", "shortest", "fewest_transfers"
    val timestamp: Long = System.currentTimeMillis()
)

