package com.confessionapp

import android.util.Log
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class TimeShow {
    fun covertTimeToText(dataDate: String?): String? {
        var convTime: String? = null
        val prefix = ""
        val suffix = "ago"
        try {
            val dateFormat =
                SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            val pasTime = dateFormat.parse(dataDate)
            val nowTime = Date()
            val dateDiff = nowTime.time - pasTime.time
            val second = TimeUnit.MILLISECONDS.toSeconds(dateDiff)
            val minute = TimeUnit.MILLISECONDS.toMinutes(dateDiff)
            val hour = TimeUnit.MILLISECONDS.toHours(dateDiff)
            val day = TimeUnit.MILLISECONDS.toDays(dateDiff)
            if (second < 60) {
                if (second == 1L){
                    convTime = "$second second $suffix"
                }else if (second == 0L){
                    convTime = "now"
                } else {
                    convTime = "$second seconds $suffix"
                }

            } else if (minute < 60) {
                if (minute==1L){
                    convTime = "$minute minute $suffix"
                } else {
                    convTime = "$minute minutes $suffix"
                }

            } else if (hour < 24) {
                if (hour==1L){
                    convTime = "$hour hour $suffix"
                } else {
                    convTime = "$hour hours $suffix"
                }


            } else if (day < 7) {
                if (day==1L){
                    convTime = "$day day $suffix"
                } else {
                    convTime = "$day days $suffix"
                }

            } else if (day >= 7) {
                val curFormater =
                    SimpleDateFormat("dd MMMM yyyy")
                val dateObj = curFormater.parse(dataDate)

                val newDateStr = curFormater.format(dateObj)
                convTime = newDateStr
            }
        } catch (e: ParseException) {
            e.printStackTrace()
            Log.e("ConvTimeE", e.message)
        }
        return convTime
    }
}