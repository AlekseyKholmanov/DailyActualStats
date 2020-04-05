package com.example.dailyactualstats.di

import androidx.room.Room
import com.example.dailyactualstats.R
import com.example.dailyactualstats.database.AppDatabase
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidApplication(),
            AppDatabase::class.java,
            androidApplication().baseContext.getString(R.string.app_name)
        )
            .fallbackToDestructiveMigrationOnDowngrade()
            .build()
    }

    single {
        get<AppDatabase>().countryDAO()
    }

    single {
        get<AppDatabase>().spreadDAO()
    }
}
