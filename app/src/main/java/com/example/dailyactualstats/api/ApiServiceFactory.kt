package com.example.dailyactualstats.api

import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object ApiServiceFactory {

    fun createSpreadService(gson: Gson): VirusSpreadService {
        return createRetrofit(gson)
            .create(VirusSpreadService::class.java)
    }

    private const val BASE_URL = "https://pomber.github.io/covid19/"

    private fun createRetrofit(gson: Gson): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)

            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(createHttpClient())
            .build()
    }

    private fun createHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
            .addNetworkInterceptor(HttpLoggingInterceptor())
            .build()
    }
}