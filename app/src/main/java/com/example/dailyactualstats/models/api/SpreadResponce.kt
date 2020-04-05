package com.example.dailyactualstats.models.api

import com.google.gson.annotations.SerializedName

/**
 * @author Alexey Kholmanov (alexey.holmanov@cleverpumpkin.ru)
 */


class Responce(
    @SerializedName("records")
    val payload: List<SpreadResponce>
)

class SpreadResponce(
    @SerializedName("day")
    val day: String,
    @SerializedName("month")
    val month: Int,
    @SerializedName("year")
    val year: Int,
    @SerializedName("countriesAndTerritories")
    val country: String,
    @SerializedName("countryterritoryCode")
    val countryCode: String,
    @SerializedName("cases")
    val cases: Int,
    @SerializedName("deaths")
    val deaths: Int
)
