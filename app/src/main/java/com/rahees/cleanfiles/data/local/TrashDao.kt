package com.rahees.cleanfiles.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TrashDao {

    @Query("SELECT * FROM trash ORDER BY trashedAt DESC")
    fun getAllTrashed(): Flow<List<TrashEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(trashEntity: TrashEntity)

    @Delete
    suspend fun delete(trashEntity: TrashEntity)

    @Query("DELETE FROM trash")
    suspend fun deleteAll()

    @Query("DELETE FROM trash WHERE expiresAt < :currentTime")
    suspend fun deleteExpired(currentTime: Long = System.currentTimeMillis())

    @Query("SELECT * FROM trash WHERE id = :id")
    suspend fun getById(id: Long): TrashEntity?
}
