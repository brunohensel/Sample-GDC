package com.example.samplegdc.application

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class GdcApplication : Application() {

    companion object {
        lateinit var instance: GdcApplication
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}