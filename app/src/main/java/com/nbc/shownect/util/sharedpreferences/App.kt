package com.nbc.shownect.util.sharedpreferences

import android.app.Application

class App : Application() {
    companion object {
        lateinit var prefs : SharedPreferencesManager
    }

    override fun onCreate() {
        prefs = SharedPreferencesManager(applicationContext)
        super.onCreate()
    }
}