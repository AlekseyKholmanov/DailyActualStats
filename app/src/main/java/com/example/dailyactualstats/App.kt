package com.example.dailyactualstats

import android.app.Application
import com.example.dailyactualstats.di.appComponent
import com.facebook.stetho.Stetho
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

/**
 * @author Alexey Kholmanov (alexey.holmanov@cleverpumpkin.ru)
 */
class App: Application() {
    override fun onCreate() {
        super.onCreate()
        configDI()
        Stetho.initializeWithDefaults(this)
    }

    private fun configDI() = startKoin {
        androidContext(this@App)
        modules(appComponent)
    }
}