package com.nbc.curtaincall.util.sharedpreferences

import android.app.Application
import android.content.Context
import android.content.SharedPreferences

class App : Application() {
    companion object {
        lateinit var prefs: SharedPreferences
        fun getPrefs(context: Context): SharedPreferences {
            return context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        }
    }

    override fun onCreate() {
        super.onCreate()
        prefs = getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
    }
}