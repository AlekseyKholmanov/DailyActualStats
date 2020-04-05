package com.example.dailyactualstats.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.dailyactualstats.models.db.SpreadEntity

/**
 * @author Alexey Kholmanov (alexey.holmanov@cleverpumpkin.ru)
 */
@Dao
interface SpreadDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(speads: List<SpreadEntity>)

    @Query("SELECT * FROM spreadentity")
    suspend fun get(): List<SpreadEntity>
}