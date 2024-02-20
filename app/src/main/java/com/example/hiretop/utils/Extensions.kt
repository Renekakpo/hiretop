package com.example.hiretop.utils

import java.util.Calendar

fun Long.toHourMinuteString(): String {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this

    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)

    return "${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}"
}