package com.samfonsec.lotr

import android.app.Application
import com.samfonsec.lotr.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class LotrApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            androidContext(this@LotrApplication)
            modules(mutableListOf(retrofitModule, authModule, moviesModule, charactersModule, viewModels))
        }
    }
}