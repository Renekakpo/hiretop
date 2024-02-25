package com.example.hiretop.ui.screens.entreprise.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.hiretop.R
import com.example.hiretop.models.UIState
import com.example.hiretop.ui.extras.FailurePopup
import com.example.hiretop.ui.extras.HireTopCircularProgressIndicator
import com.example.hiretop.viewModels.EnterpriseViewModel

@Composable
fun EnterpriseDashboardScreen(
    modifier: Modifier = Modifier,
    enterpriseViewModel: EnterpriseViewModel = hiltViewModel()
) {
    val enterpriseProfileId by enterpriseViewModel.enterpriseProfileId.collectAsState(initial = null)
    val retention by enterpriseViewModel.retention.collectAsState()
    val conversion by enterpriseViewModel.conversion.collectAsState()
    val productivity by enterpriseViewModel.productivity.collectAsState()
    val views by enterpriseViewModel.views.collectAsState()
    val applications by enterpriseViewModel.applications.collectAsState()
    val hired by enterpriseViewModel.hired.collectAsState()
    val interviews by enterpriseViewModel.interviews.collectAsState()
    var onErrorMessage by remember { mutableStateOf<String?>(null) }
    var uiState by remember { mutableStateOf(UIState.LOADING) }

    if (!onErrorMessage.isNullOrEmpty()) {
        FailurePopup(errorMessage = "$onErrorMessage", onDismiss = {
            onErrorMessage = null
        })
    }

    LaunchedEffect(enterpriseProfileId) {
        uiState = if (enterpriseProfileId == null) {
            UIState.FAILURE
        } else {
            enterpriseViewModel.calculateRetention(
                "$enterpriseProfileId",
                onSuccess = {},
                onFailure = {})
            enterpriseViewModel.calculateConversion(
                "$enterpriseProfileId",
                onSuccess = {},
                onFailure = {})
            enterpriseViewModel.calculateProductivity(
                "$enterpriseProfileId",
                onSuccess = {},
                onFailure = {})
            enterpriseViewModel.calculateViews(
                "$enterpriseProfileId",
                onSuccess = {},
                onFailure = {})
            enterpriseViewModel.calculateApplications(
                "$enterpriseProfileId",
                onSuccess = {},
                onFailure = {})
            enterpriseViewModel.calculateHires(
                "$enterpriseProfileId",
                onSuccess = {},
                onFailure = {})
            enterpriseViewModel.calculateInterviews(
                "$enterpriseProfileId",
                onSuccess = {},
                onFailure = {})
            UIState.SUCCESS
        }
    }

    when (uiState) {
        UIState.LOADING -> {
            // Display loader while fetching data
            HireTopCircularProgressIndicator()
        }

        UIState.FAILURE -> {
            Text(
                text = stringResource(R.string.empty_enterprise_analytics_text),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                textAlign = TextAlign.Center
            )
        }

        UIState.SUCCESS -> {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.background)
                    .padding(top = 15.dp, start = 15.dp, end = 15.dp)
            ) {
                AnalyticsDashboard(
                    views = views,
                    applications = applications,
                    hires = hired,
                    interviews = interviews
                )

                Spacer(modifier = Modifier.height(height = 20.dp))

                KeyPerformanceIndicators(
                    kpis = listOf(
                        stringResource(R.string.retention_text) to "$retention%",
                        stringResource(R.string.conversion_text) to "$conversion%",
                        stringResource(R.string.productivity_text) to "$productivity%"
                    )
                )
            }
        }
    }
}