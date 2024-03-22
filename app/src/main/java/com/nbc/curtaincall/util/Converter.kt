package com.nbc.curtaincall.util

import java.time.LocalDate

object Converter {
    private val date = LocalDate.now()
    fun nowDateFormat(): String {

        return date.toString().split("-").joinToString("")
    }

    //현재 날짜 하루 전 
    fun nowDateOneDayAgo(): String {
        return (date.toString().split("-").joinToString("").toInt() - 1).toString()
    }

    //현재 날짜 한 달 후
    fun oneMonthFromNow(): String {
        return (date.toString().split("-").joinToString("").toInt() + 100).toString()
    }
}