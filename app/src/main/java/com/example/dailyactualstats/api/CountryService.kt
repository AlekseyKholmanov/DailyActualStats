package com.example.dailyactualstats.api

import com.example.dailyactualstats.models.api.CountryServiceResponse
import retrofit2.http.GET

/**
 * @author Alexey Kholmanov (alexey.holmanov@cleverpumpkin.ru)
 */
interface CountryService {

    @GET("all")
    suspend fun getCountriesInfo(): List<CountryServiceResponse>
}