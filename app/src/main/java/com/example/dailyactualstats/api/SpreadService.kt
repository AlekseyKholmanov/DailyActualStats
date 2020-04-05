package com.example.dailyactualstats.api

import com.example.dailyactualstats.models.api.SpreadServiceResponse
import retrofit2.http.GET


interface SpreadService {

    @GET("casedistribution/json/")
    suspend fun getSpread():SpreadServiceResponse
}