package com.example.dailyactualstats.models.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @author Alexey Kholmanov (alexey.holmanov@cleverpumpkin.ru)
 */
@Entity
data class CacheMarker(

    @PrimaryKey
    @ColumnInfo(name = "cacheKey")
    val cacheKey: String,

    @ColumnInfo(name = "timestamp")
    val timestamp: Long
)