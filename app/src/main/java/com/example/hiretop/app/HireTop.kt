package com.example.hiretop.app

import android.app.Application
import android.content.Context

class HireTop: Application() {
    companion object {
        lateinit var appContext: Context
            private set
    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
    }
}