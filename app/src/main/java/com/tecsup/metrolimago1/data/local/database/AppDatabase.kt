package com.tecsup.metrolimago1.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tecsup.metrolimago1.data.local.FavoriteStation
import com.tecsup.metrolimago1.data.local.dao.FavoriteStationDao

@Database(entities = [FavoriteStation::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteStationDao(): FavoriteStationDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "metrolima_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}