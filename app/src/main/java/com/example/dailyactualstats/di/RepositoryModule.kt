package com.example.dailyactualstats.di

import com.example.dailyactualstats.repository.CountryRepository
import com.example.dailyactualstats.repository.SpreadRepository
import org.koin.dsl.module

/**
 * @author Alexey Kholmanov (alexey.holmanov@cleverpumpkin.ru)
 */
val repositoryModule = module {

    single { SpreadRepository(get(), get()) }
    single { CountryRepository(get(), get()) }

}