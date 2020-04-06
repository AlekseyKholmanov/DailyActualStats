package com.example.dailyactualstats.repository

import com.example.dailyactualstats.api.CountryService
import com.example.dailyactualstats.database.dao.CountryDao
import com.example.dailyactualstats.models.db.CountryEntity
import com.example.dailyactualstats.models.db.toEntity

/**
 * @author Alexey Kholmanov (alexey.holmanov@cleverpumpkin.ru)
 */
class CountryRepository(val dao: CountryDao, val api: CountryService) {

    suspend fun getCountryInfo(): List<CountryEntity> {
        return if (getCountryFromDb().isNullOrEmpty()) getCountryFromApi() else getCountryFromDb()!!
    }

    private suspend fun getCountryFromApi(): List<CountryEntity> {
        return api.getCountriesInfo()
            .map { it.toEntity() }
            .also {
                dao.add(it)
            }
    }

    private suspend fun getCountryFromDb(): List<CountryEntity>? {
        return dao.getAll()
    }
}