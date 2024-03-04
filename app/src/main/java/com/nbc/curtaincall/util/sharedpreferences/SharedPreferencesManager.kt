package com.nbc.curtaincall.util.sharedpreferences

import android.content.Context
import com.google.android.material.chip.Chip
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
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

    // 검색 필터 조건 저장하기
    fun saveSearchFilter(searchFilter: List<Pair<Chip,String>?>?) {
        val searchFilter = gson.toJson(searchFilter)
        prefs.edit().putString(Constants.SEARCH_FILTER, searchFilter).apply()
    }
    // 검색 필터 조건 불러오기
    fun loadSearchFilter() : List<Pair<Chip,String>?>?{
        val searchFilter = prefs.getString(Constants.SEARCH_FILTER, null)
        return if(searchFilter != null) {
            val type = object : TypeToken<List<Pair<Chip, String>?>>() {}.type
            gson.fromJson(searchFilter, type)
        } else {
            null
        }
    }

}