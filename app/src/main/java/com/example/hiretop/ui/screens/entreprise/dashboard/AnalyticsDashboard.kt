package com.example.hiretop.ui.screens.entreprise.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun AnalyticsDashboard(
    views: Int,
    conversions: Int,
    applications: Int,
    hires: Int,
    interviews: Int
) {
    Card(
        modifier = Modifier.wrapContentSize(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.background)
                .padding(15.dp)
        ) {
            Text(
                text = "Tableaux de Bord Analytiques",
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(height = 15.dp))

            AnalyticsItem(title = "Vues d'Offres d'Emploi", value = views.toString())

            Spacer(modifier = Modifier.height(height = 5.dp))

            AnalyticsItem(title = "Taux de Conversion des Candidatures", value = "$conversions%")

            Spacer(modifier = Modifier.height(height = 5.dp))

            AnalyticsItem(title = "Candidatures", value = applications.toString())

            Spacer(modifier = Modifier.height(height = 5.dp))

            AnalyticsItem(title = "Entretiens Planifiés", value = interviews.toString())

            Spacer(modifier = Modifier.height(height = 5.dp))

            AnalyticsItem(title = "Recrutements", value = hires.toString())
        }
    }
}

@Composable
fun AnalyticsItem(title: String, value: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp)
        )

        Spacer(modifier = Modifier.width(10.dp))

        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}