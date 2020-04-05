package com.example.dailyactualstats.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.dailyactualstats.models.db.CacheMarker

/**
 * @author Alexey Kholmanov (alexey.holmanov@cleverpumpkin.ru)
 */

@Dao
interface CacheMarkerDao {

    @Query("SELECT * FROM cachemarker WHERE cacheKey=:key")
    fun get(key: String): CacheMarker?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg markers: CacheMarker)
}