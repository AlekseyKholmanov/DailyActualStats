package com.example.dailyactualstats.models.api

import com.google.gson.annotations.SerializedName

/**
 * @author Alexey Kholmanov (alexey.holmanov@cleverpumpkin.ru)
 */
class CountryServiceResponse(
    @SerializedName("name")
    val country: String,
    @SerializedName("alpha3Code")
    val code: String,
    @SerializedName("population")
    val population: Int,
    @SerializedName("region")
    val region: String,
    @SerializedName("flag")
    val flagUrl: String
)