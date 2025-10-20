package com.tecsup.metrolimago1.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tecsup.metrolimago1.data.local.FavoriteStation
import com.tecsup.metrolimago1.data.local.dao.FavoriteStationDao

@Database(entities = [FavoriteStation::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteStationDao(): FavoriteStationDao
}