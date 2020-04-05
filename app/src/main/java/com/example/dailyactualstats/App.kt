package com.example.dailyactualstats

import android.app.Application
import com.example.dailyactualstats.di.appComponent
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

/**
 * @author Alexey Kholmanov (alexey.holmanov@cleverpumpkin.ru)
 */
class App: Application() {
    override fun onCreate() {
        super.onCreate()
        configDI()
    }

    private fun configDI() = startKoin {
        androidContext(this@App)
        modules(appComponent)
    }
}