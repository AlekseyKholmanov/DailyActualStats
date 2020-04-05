package com.example.dailyactualstats.repository

import com.example.dailyactualstats.database.AppDatabase
import com.example.dailyactualstats.models.db.CacheMarker
import org.joda.time.DateTime
import org.joda.time.Duration

/**
 * @author Alexey Kholmanov (alexey.holmanov@cleverpumpkin.ru)
 */
abstract class BaseRepository<Dao>(private val database: AppDatabase) {

    protected val dao: Dao by lazy { getDatabaseAccessObject(database) }

    protected abstract fun getDatabaseAccessObject(database: AppDatabase): Dao

    protected fun updateCacheTimestamp(keys: List<String>) {
        val markers = keys
            .map { key ->
                CacheMarker(key, DateTime().withTimeAtStartOfDay().millis)
            }
            .toTypedArray()

        database.cacheMarkerDAO().insertAll(*markers)
    }

    protected fun updateCacheTimestamp(key: String = defaultCacheKey) {
        database.cacheMarkerDAO().insertAll(CacheMarker(key, DateTime().withTimeAtStartOfDay().millis))
    }

    protected suspend fun isCacheActualAsync(key: String = defaultCacheKey): Boolean {
        return isCacheActual(key)
    }

    protected suspend fun isCacheActual(key: String = defaultCacheKey): Boolean {
        val marker = database.cacheMarkerDAO().get(key) ?: return false
        val now = System.currentTimeMillis()
        return now - marker.timestamp < cacheDuration.millis
    }

    abstract val cacheDuration: Duration

    abstract val defaultCacheKey: String
}