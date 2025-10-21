package com.tecsup.metrolimago1.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tecsup.metrolimago1.data.local.FavoriteStation
import com.tecsup.metrolimago1.data.local.StationEntity
import com.tecsup.metrolimago1.data.local.dao.FavoriteStationDao
import com.tecsup.metrolimago1.data.local.dao.StationDao

@Database(entities = [StationEntity::class, FavoriteStation::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun stationDao(): StationDao
    abstract fun favoriteStationDao(): FavoriteStationDao
}