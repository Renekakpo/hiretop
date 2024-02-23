package com.example.hiretop.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.example.hiretop.R
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

object Utils {
    fun getYearsList(): List<String> {
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val startYear = 1924
        val yearsList = mutableListOf<String>()

        for (year in startYear..currentYear) {
            yearsList.add(year.toString())
        }

        yearsList.reverse()
        yearsList.add(0, "Année")

        return yearsList
    }

    fun compressImage(context: Context, uri: Uri, filename: String): File? {
        try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)

            val file = File(context.cacheDir, filename)
            val outputStream = FileOutputStream(file)

            // Compress the bitmap into PNG format
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, outputStream)
            outputStream.flush()
            outputStream.close()

            return file
        } catch (e: Exception) {
            // TODO: Handle exception
            return null
        }
    }

    fun getPostedTimeAgo(context: Context, timestamp: Long): String {
        val currentTime = System.currentTimeMillis()
        val timeDiff = currentTime - timestamp

        val seconds = TimeUnit.MILLISECONDS.toSeconds(timeDiff)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(timeDiff)
        val hours = TimeUnit.MILLISECONDS.toHours(timeDiff)
        val days = TimeUnit.MILLISECONDS.toDays(timeDiff)

        return when {
            seconds < 60 -> context.getString(R.string.posted_seconds_ago_text, seconds)
            minutes < 60 -> context.getString(R.string.posted_minutes_ago_text, minutes)
            hours < 24 -> context.getString(R.string.posted_hours_ago_text, hours)
            days < 7 -> context.getString(R.string.posted_days_ago_text, days)
            else -> context.getString(R.string.posted_weeks_ago_text, days / 7)
        }
    }

    fun getAppliedTimeAgo(context: Context, timestamp: Long): String {
        val currentTime = System.currentTimeMillis()
        val timeDiff = currentTime - timestamp

        val seconds = TimeUnit.MILLISECONDS.toSeconds(timeDiff)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(timeDiff)
        val hours = TimeUnit.MILLISECONDS.toHours(timeDiff)
        val days = TimeUnit.MILLISECONDS.toDays(timeDiff)

        return when {
            seconds < 60 -> context.getString(R.string.applied_seconds_ago_text, seconds)
            minutes < 60 -> context.getString(R.string.applied_minutes_ago_text, minutes)
            hours < 24 -> context.getString(R.string.applied_hours_ago_text, hours)
            days < 7 -> context.getString(R.string.applied_days_ago_text, days)
            else -> context.getString(R.string.applied_weeks_ago_text, days / 7)
        }
    }

    fun formatDate(timestamp: Long): String {
        val date = Date(timestamp)
        val sdf = SimpleDateFormat("dd - MMMM - yyyy 'à' HH'h'mm", Locale.FRENCH)
        return sdf.format(date)
    }

    fun getFormattedDateOrHour(timestamp: Long): String {
        val calendar = Calendar.getInstance()
        val today = calendar.clone() as Calendar
        today.set(Calendar.HOUR_OF_DAY, 0)
        today.set(Calendar.MINUTE, 0)
        today.set(Calendar.SECOND, 0)
        today.set(Calendar.MILLISECOND, 0)

        val yesterday = calendar.clone() as Calendar
        yesterday.add(Calendar.DAY_OF_YEAR, -1)
        yesterday.set(Calendar.HOUR_OF_DAY, 0)
        yesterday.set(Calendar.MINUTE, 0)
        yesterday.set(Calendar.SECOND, 0)
        yesterday.set(Calendar.MILLISECOND, 0)

        calendar.timeInMillis = timestamp

        return when {
            calendar.after(today) -> {
                val hourMinuteFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                hourMinuteFormat.format(calendar.time)
            }
            calendar.after(yesterday) -> "Hier"
            else -> {
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                dateFormat.format(calendar.time)
            }
        }
    }

    /**
     * Function to convert month names to numbers for comparison
     */
    fun monthToNumber(month: String): String {
        return when (month.lowercase(Locale.getDefault())) {
            "janvier" -> "01"
            "février" -> "02"
            "mars" -> "03"
            "avril" -> "04"
            "mai" -> "05"
            "juin" -> "06"
            "juillet" -> "07"
            "août" -> "08"
            "septembre" -> "09"
            "octobre" -> "10"
            "novembre" -> "11"
            "décembre" -> "12"
            else -> "00" // Default to 00 for invalid months
        }
    }
}