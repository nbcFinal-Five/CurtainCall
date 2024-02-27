package com.nbc.curtaincall.util

import java.time.LocalDate

object Converter {
    fun nowDateFormat():String{
        val date = LocalDate.now()
        return date.toString().split("-").joinToString("")
    }
}