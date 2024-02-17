package com.example.hiretop.utils

import java.util.Calendar

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
}