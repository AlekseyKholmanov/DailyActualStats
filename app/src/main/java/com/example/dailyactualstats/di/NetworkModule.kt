package com.example.dailyactualstats.di

import com.example.dailyactualstats.api.VirusSpreadService
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.*

val networkModule = module {

    single {
        get<Retrofit>().create<VirusSpreadService>()
    }

    single<OkHttpClient> {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
            .connectionSpecs(
                Collections.singletonList(
                    ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                        .allEnabledCipherSuites()
                        .build()
                )
            )
            .addNetworkInterceptor(HttpLoggingInterceptor())
            .build()
    }

    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl("https://opendata.ecdc.europa.eu/covid19/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
    }
}
