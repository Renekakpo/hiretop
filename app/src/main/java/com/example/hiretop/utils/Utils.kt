package com.example.hiretop.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.example.hiretop.R
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
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
}