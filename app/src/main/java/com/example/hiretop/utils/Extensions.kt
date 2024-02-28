package com.example.hiretop.utils

import java.util.Calendar

/**
 * Converts a Long value representing milliseconds since epoch to a formatted string in "HH:mm" format (hours:minutes).
 * @return The formatted time string.
 */
fun Long.toHourMinuteString(): String {
    // Create a Calendar instance and set its time to the given milliseconds
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this

    // Extract the hour and minute components from the Calendar instance
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)

    // Format the hour and minute components to ensure they have two digits (e.g., 09:05)
    return "${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}"
}
