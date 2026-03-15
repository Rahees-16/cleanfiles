package com.rahees.cleanfiles.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trash")
data class TrashEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val originalPath: String,
    val trashPath: String,
    val name: String,
    val size: Long,
    val trashedAt: Long = System.currentTimeMillis(),
    val expiresAt: Long = System.currentTimeMillis() + 30L * 24 * 60 * 60 * 1000 // 30 days
)
