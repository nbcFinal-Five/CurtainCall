package com.nbc.curtaincall.util.sharedpreferences

import android.content.Context
import com.google.gson.Gson
import com.nbc.curtaincall.util.Constants
import com.tickaroo.tikxml.TikXml

class SharedPreferencesManager(context: Context) {
    private val prefs = context.getSharedPreferences(Constants.PREFS_FILENAME, Context.MODE_PRIVATE)
    private val gson = Gson()
    private val tikXml = TikXml.Builder().exceptionOnUnreadXml(false).build()

    fun saveString(string : String) {
        prefs.edit().putString(Constants.KEY_FAVORITE,string).apply()
    }

}