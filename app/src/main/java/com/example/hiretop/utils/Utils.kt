package com.example.hiretop.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.example.hiretop.R
import com.example.hiretop.app.HireTop.Companion.appContext
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

/**
 * Contains utility functions for various tasks such as date formatting, image compression, string extraction, etc.
 */
object Utils {
    /**
     * Generates a list of years from 1924 to the current year, with "Année" as the first element.
     * @return List<String> containing years in descending order.
     */
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

    /**
     * Compresses the provided image and saves it to a temporary file.
     * @param context The application context.
     * @param uri The URI of the image to compress.
     * @param filename The name of the compressed image file.
     * @param callback A callback function to handle errors.
     * @return File object representing the compressed image.
     */
    fun compressImage(
        context: Context,
        uri: Uri,
        filename: String,
        callback: (String) -> Unit
    ): File? {
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
            callback(e.message ?: appContext.getString(R.string.unknown_error_text))
            return null
        }
    }

    /**
     * Converts a timestamp into a human-readable "time ago" format.
     * @param context The application context.
     * @param timestamp The timestamp to convert.
     * @return A string representing the time difference in a human-readable format.
     */
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

    /**
     * Converts a timestamp into a human-readable "time ago" format for job application.
     * @param context The application context.
     * @param timestamp The timestamp to convert.
     * @return A string representing the time difference in a human-readable format.
     */
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

    /**
     * Formats the given timestamp into a string with the specified format.
     * @param timestamp The timestamp to format.
     * @return A string representing the formatted date.
     */
    fun formatDate(timestamp: Long): String {
        val date = Date(timestamp)
        val sdf = SimpleDateFormat("dd - MMMM - yyyy 'à' HH'h'mm", Locale.FRENCH)
        return sdf.format(date)
    }

    /**
     * Converts a month name (in French) to its corresponding numeric representation.
     * @param month The month name to convert.
     * @return A string representing the numeric representation of the month.
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

    /**
     * Extracts a string from a given link using regex.
     * @param link The link from which to extract the string.
     * @return The extracted string or null if no match is found.
     */
    fun extractStringFromLink(link: String): String? {
        if (link.isEmpty()) {
            return link
        }

        val regex = Regex("""/images%2F([\w-]+)\?""")
        val matchResult = regex.find(link)
        return matchResult?.groupValues?.getOrNull(1)
    }

    /**
     * Validates an email address.
     * @param email The email address to validate.
     * @return True if the email address is valid, false otherwise.
     */
    fun isEmailValid(email: String): Boolean {
        val emailRegex = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"
        return email.matches(emailRegex.toRegex())
    }

    /**
     * Validates a password based on defined criteria.
     * @param password The password to validate.
     * @return True if the password meets all criteria, false otherwise.
     */
    fun isValidPassword(password: String): Boolean {
        // Define your criteria for a valid password
        val minLength = 8
        val maxLength = 20
        val containsUpperCase = password.any { it.isUpperCase() }
        val containsLowerCase = password.any { it.isLowerCase() }
        val containsDigit = password.any { it.isDigit() }
        val containsSpecialChar = password.any { !it.isLetterOrDigit() }

        // Check if the password meets all criteria
        return password.length in minLength..maxLength &&
                containsUpperCase &&
                containsLowerCase &&
                containsDigit &&
                containsSpecialChar
    }
}