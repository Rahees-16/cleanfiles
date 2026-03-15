package com.rahees.cleanfiles.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RecentDao {

    @Query("SELECT * FROM recent_files ORDER BY accessedAt DESC LIMIT :limit")
    fun getRecentFiles(limit: Int = 10): Flow<List<RecentFileEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecent(recent: RecentFileEntity)

    @Query("DELETE FROM recent_files WHERE path = :path")
    suspend fun deleteByPath(path: String)

    @Query("DELETE FROM recent_files WHERE id NOT IN (SELECT id FROM recent_files ORDER BY accessedAt DESC LIMIT :keep)")
    suspend fun trimToSize(keep: Int = 50)

    @Query("DELETE FROM recent_files")
    suspend fun clearAll()
}
