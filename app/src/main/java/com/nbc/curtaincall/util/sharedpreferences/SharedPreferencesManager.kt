package com.nbc.curtaincall.util.sharedpreferences

import android.content.Context
import com.google.gson.Gson
import com.nbc.curtaincall.util.Constants
import com.tickaroo.tikxml.TikXml

class SharedPreferencesManager(context: Context) {
    private val prefs = context.getSharedPreferences(Constants.PREFS_FILENAME, Context.MODE_PRIVATE)
    private val gson = Gson()
    private val tikXml = TikXml.Builder().exceptionOnUnreadXml(false).build()

    // 검색창 단어 저장
    fun saveSearchWord(word : String) {
        prefs.edit().putString(Constants.SEARCH_WORD,word).apply()
    }
    // 저장된 검색어 불러오기
    fun loadSearchWord() :String {
        return prefs.getString(Constants.SEARCH_WORD, Constants.DEFAULT_STRING) ?:""
    }

}