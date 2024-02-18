package com.example.hiretop.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import java.util.Calendar
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

    fun getTimeAgo(timestamp: Long): String {
        val currentTime = System.currentTimeMillis()
        val timeDiff = currentTime - timestamp

        val seconds = TimeUnit.MILLISECONDS.toSeconds(timeDiff)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(timeDiff)
        val hours = TimeUnit.MILLISECONDS.toHours(timeDiff)
        val days = TimeUnit.MILLISECONDS.toDays(timeDiff)

        return when {
            seconds < 60 -> "Posted $seconds seconds ago"
            minutes < 60 -> "Posted $minutes minutes ago"
            hours < 24 -> "Posted $hours hours ago"
            days < 7 -> "Posted $days days ago"
            else -> "Posted ${days / 7} weeks ago"
        }
    }
}