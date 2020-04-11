package com.example.dailyactualstats.repository

import com.example.dailyactualstats.api.SpreadService
import com.example.dailyactualstats.database.AppDatabase
import com.example.dailyactualstats.database.dao.SpreadDao
import com.example.dailyactualstats.models.db.SpreadEntity
import com.example.dailyactualstats.models.db.toEntity
import org.joda.time.Duration

/**
 * @author Alexey Kholmanov (alexey.holmanov@cleverpumpkin.ru)
 */
class SpreadRepository(
    database: AppDatabase,
    private val api: SpreadService
) : BaseRepository<SpreadDao>(database) {

    override fun getDatabaseAccessObject(database: AppDatabase): SpreadDao = database.spreadDAO()

    override val cacheDuration: Duration = Duration.standardHours(12)
    override val defaultCacheKey: String = "SpreadRepository"

    suspend fun getSpreadInfo(force:Boolean = false): List<SpreadEntity> {
        if(force){
            return getSpreadFromApi()
        }
        return if (isCacheActualAsync()) {
            getSpreadFromDb() ?: getSpreadFromApi()
        } else{
            getSpreadFromApi()
        }
    }

    private suspend fun getSpreadFromDb(): List<SpreadEntity>? {
        return dao.getAll()
    }

    private suspend fun getSpreadFromApi(): List<SpreadEntity> {
        return api.getSpread().payload.map {
            it.toEntity()
        }.also {
            dao.invalidate(it)
            updateCacheTimestamp()
        }
    }
}