package com.example.flatline

import android.app.Application
import com.example.flatline.common.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class FlatlineApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@FlatlineApplication)
            modules(appModule)
        }
    }
}