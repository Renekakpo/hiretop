package com.example.hiretop.ui.screens.entreprise.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun EnterpriseDashboardScreen(modifier: Modifier = Modifier) {
    val kpis = listOf(
        "Retention" to "80%",
        "Conversion" to "60%",
        "Satisfaction" to "85%",
        "Productivité" to "90%"
    )

//    val sampleActivities = listOf(
//        ActivityItem("2024-02-14 10:00 AM", "Updated company profile"),
//        ActivityItem("2024-02-13 3:30 PM", "Posted new job opening"),
//        ActivityItem("2024-02-12 9:45 AM", "Reviewed candidate application"),
//        ActivityItem("2024-02-11 2:15 PM", "Scheduled interview with candidate")
//    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(top = 15.dp, start = 15.dp, end = 15.dp)
    ) {
        AnalyticsDashboard(
            views = 15,
            conversions = 25,
            applications = 24,
            hires = 5,
            interviews = 18
        )

        Spacer(modifier = Modifier.height(height = 20.dp))

        KeyPerformanceIndicators(kpis = kpis)

//        Spacer(modifier = Modifier.height(height = 20.dp))
//
//        ActivityHistory(activities = sampleActivities)

    }
}