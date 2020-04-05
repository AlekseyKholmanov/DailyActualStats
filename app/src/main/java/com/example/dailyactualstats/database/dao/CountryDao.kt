package com.example.dailyactualstats.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.dailyactualstats.models.db.CountryEntity

/**
 * @author Alexey Kholmanov (alexey.holmanov@cleverpumpkin.ru)
 */

@Dao
interface CountryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(countries: List<CountryEntity>)

    @Query("SELECT * FROM countryentity")
    suspend fun get(): List<CountryEntity>
}