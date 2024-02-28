package com.example.hiretop.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.hiretop.R
import com.example.hiretop.navigation.CandidateBottomNavGraph
import com.example.hiretop.navigation.EnterpriseBottomNavGraph
import com.example.hiretop.navigation.NavDestination
import com.example.hiretop.viewModels.MainViewModel

object AccountTypeScreen : NavDestination {
    override val route: String = "account_type_screen"
}

@Composable
fun AccountTypeScreen(
    navController: NavHostController,
    mainViewModel: MainViewModel = hiltViewModel()
) {
    val mWidth = LocalConfiguration.current.screenWidthDp.dp

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.create_account_text),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier
                .padding(horizontal = 15.dp)
        )

        Spacer(modifier = Modifier.height(height = 30.dp))

        Text(
            text = stringResource(R.string.account_type_subheader_text),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier
                .padding(horizontal = 15.dp)
                .align(alignment = Alignment.Start)
        )

        Spacer(modifier = Modifier.height(height = 10.dp))

        Text(
            text = stringResource(R.string.account_type_info_text),
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier
                .padding(horizontal = 15.dp)
        )

        Spacer(modifier = Modifier.height(height = mWidth * 0.1F))

        OutlinedButton(
            modifier = Modifier
                .width(width = mWidth * 0.7F)
                .height(45.dp)
                .padding(horizontal = 10.dp),
            shape = MaterialTheme.shapes.small,
            border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.primary),
            onClick = {
                mainViewModel.saveAccountType(value = false)
                onNavigateToNextScreen(navController, CandidateBottomNavGraph.route)
            }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.talent_button_text),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }

        Spacer(modifier = Modifier.height(height = 25.dp))

        Button(
            modifier = Modifier
                .width(width = mWidth * 0.7F)
                .height(45.dp)
                .padding(horizontal = 10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            shape = MaterialTheme.shapes.small,
            onClick = {
                mainViewModel.saveAccountType(value = true)
                onNavigateToNextScreen(navController, EnterpriseBottomNavGraph.route)
            }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.button_entreprise_text),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }

    }
}

private fun onNavigateToNextScreen(navController: NavHostController, destination: String) {
    navController.navigate(route = destination) {
        popUpTo(route = AccountTypeScreen.route) {
            inclusive = true
        }
    }
}
