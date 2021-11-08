package com.sofudev.sipphcheck.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

class DateConvert {
    fun convertToDate(input: String) : String{
        val split = input.split("-")
        return "${split[2]}-${split[1]}-${split[0]}"
    }

    @SuppressLint("SimpleDateFormat")
    fun convertCalendarToDate(input : Calendar) : String{
        val date: Date = input.time
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        return sdf.format(date)
    }
}