package com.moviesafe

import android.app.Application
import initKoin
import org.koin.core.context.startKoin

class MainApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        initKoin()
    }
}