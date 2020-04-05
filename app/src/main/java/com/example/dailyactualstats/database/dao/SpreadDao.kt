package com.example.dailyactualstats.database.dao

import androidx.room.*
import com.example.dailyactualstats.models.db.SpreadEntity

/**
 * @author Alexey Kholmanov (alexey.holmanov@cleverpumpkin.ru)
 */
@Dao
interface SpreadDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(speads: List<SpreadEntity>)


    @Query("DELETE FROM SpreadEntity")
    fun deleteAll()

    @Query("SELECT * FROM SpreadEntity")
    suspend fun getAll(): List<SpreadEntity>

    @Transaction
    fun invalidate(spread:List<SpreadEntity>){
        deleteAll()
        add(spread)
    }
}