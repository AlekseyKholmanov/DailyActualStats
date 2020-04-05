package com.example.dailyactualstats.models.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.dailyactualstats.models.api.CountryServiceResponse

/**
 * @author Alexey Kholmanov (alexey.holmanov@cleverpumpkin.ru)
 */
@Entity
class CountryEntity (
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val code: String,
    val population: Int,
    val region: String,
    val flagUrl: String
)

fun CountryServiceResponse.toEntity(): CountryEntity {
    return CountryEntity(country,code, population, region, flagUrl)
}