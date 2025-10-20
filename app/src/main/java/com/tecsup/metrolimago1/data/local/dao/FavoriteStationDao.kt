package com.tecsup.metrolimago1.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tecsup.metrolimago1.data.local.FavoriteStation
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteStationDao {
    @Query("SELECT * FROM favorite_stations")
    fun getAll(): Flow<List<FavoriteStation>>

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insert(station: FavoriteStation)
}