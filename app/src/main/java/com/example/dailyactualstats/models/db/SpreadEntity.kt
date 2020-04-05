package com.example.dailyactualstats.models.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.dailyactualstats.models.api.SpreadInfo
import org.joda.time.DateTime

/**
 * @author Alexey Kholmanov (alexey.holmanov@cleverpumpkin.ru)
 */
@Entity
data class SpreadEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val date: DateTime,
    val country: String,
    val countryCode: String,
    val cases: Int,
    val deaths: Int
)

fun SpreadInfo.toEntity(): SpreadEntity {
    return SpreadEntity(
        id = "$fullDate$countryCode",
        date = DateTime(year,month,day,0,0),
        cases = cases,
        country = country,
        countryCode = countryCode,
        deaths = deaths
    )
}