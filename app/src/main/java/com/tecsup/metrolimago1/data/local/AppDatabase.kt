package com.tecsup.metrolimago1.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [FavoriteStation::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteStationDao(): FavoriteStationDao
}