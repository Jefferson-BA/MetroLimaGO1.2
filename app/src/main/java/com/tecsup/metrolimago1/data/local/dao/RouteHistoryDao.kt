package com.tecsup.metrolimago1.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tecsup.metrolimago1.data.local.RouteHistory
import kotlinx.coroutines.flow.Flow

@Dao
interface RouteHistoryDao {
    @Query("SELECT * FROM route_history ORDER BY timestamp DESC")
    fun getAll(): Flow<List<RouteHistory>>

    @Query("SELECT * FROM route_history ORDER BY timestamp DESC LIMIT :limit")
    fun getRecent(limit: Int): Flow<List<RouteHistory>>

    @Query("SELECT * FROM route_history WHERE id = :id")
    suspend fun getById(id: Long): RouteHistory?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(route: RouteHistory): Long

    @Delete
    suspend fun delete(route: RouteHistory)

    @Query("DELETE FROM route_history WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM route_history")
    suspend fun deleteAll()
}

