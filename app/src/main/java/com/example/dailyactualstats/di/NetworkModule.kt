package com.example.dailyactualstats.di

import com.example.dailyactualstats.api.CountryService
import com.example.dailyactualstats.api.SpreadService
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.*

val networkModule = module {

    single<OkHttpClient> { okHTTP() }

    single(named("country")) { retrofit(get(), COUNTRY_URL) }
    single(named("covid")) { retrofit(get(), SPREAD_URL) }

    single { get<Retrofit>(named("covid")).create<SpreadService>() }
    single { get<Retrofit>(named("country")).create<CountryService>() }
}

const val SPREAD_URL = "https://opendata.ecdc.europa.eu/covid19/"
const val COUNTRY_URL = "https://restcountries.eu/rest/v2/"

fun retrofit(client: OkHttpClient, baseUrl: String): Retrofit {
    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()
}

fun okHTTP(): OkHttpClient {
    return OkHttpClient.Builder()
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
