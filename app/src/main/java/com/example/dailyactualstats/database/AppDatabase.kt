package com.example.dailyactualstats.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.dailyactualstats.database.dao.CountryDao
import com.example.dailyactualstats.database.dao.SpreadDao
import com.example.dailyactualstats.models.db.CountryEntity
import com.example.dailyactualstats.models.db.SpreadEntity

@Database(entities = [CountryEntity::class, SpreadEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun countryDAO(): CountryDao
    abstract fun spreadDAO(): SpreadDao
}
