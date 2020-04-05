package com.example.dailyactualstats.api

import com.example.dailyactualstats.models.api.Responce
import com.example.dailyactualstats.models.api.SpreadResponce
import retrofit2.http.GET


interface VirusSpreadService {

    @GET("casedistribution/json/")
    suspend fun getSpread():Responce
}