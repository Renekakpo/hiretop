package com.example.hiretop.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowRightAlt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.hiretop.R
import com.example.hiretop.models.UIState
import com.example.hiretop.navigation.CandidateBottomNavGraph
import com.example.hiretop.navigation.EnterpriseBottomNavGraph
import com.example.hiretop.navigation.NavDestination
import com.example.hiretop.ui.extras.HireTopCircularProgressIndicator
import com.example.hiretop.ui.extras.NetworkIssueScreen
import com.example.hiretop.ui.screens.auth.LoginScreen
import com.example.hiretop.viewModels.MainViewModel

object WelcomeScreen : NavDestination {
    override val route: String = "welcome_screen"
}

@Composable
fun WelcomeScreen(
    navController: NavHostController,
    mainViewModel: MainViewModel = hiltViewModel()
) {
    // Collect flows and observe its values
    val isNetworkAvailable by mainViewModel.isNetworkAvailable.collectAsState(null)
    val isEnterpriseAccount by mainViewModel.isEnterpriseAccount.collectAsState(null)
    var uiState by remember { mutableStateOf(UIState.LOADING) }

    val mWidth = LocalConfiguration.current.screenWidthDp.dp
    val mHeight = LocalConfiguration.current.screenHeightDp.dp

    LaunchedEffect(isEnterpriseAccount) {
        uiState = if (isNetworkAvailable == true) {
            if (isEnterpriseAccount != null) {
                UIState.SUCCESS
            } else {
                UIState.FAILURE
            }
        } else {
            UIState.FAILURE
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.primary),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(weight = 1f))

        Text(
            text = stringResource(id = R.string.app_name),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.padding(horizontal = 15.dp)
        )

        Spacer(modifier = Modifier.height(height = mHeight / 4))

        Text(
            text = stringResource(R.string.welcome_screen_headline_text),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.padding(horizontal = 15.dp)
        )

        Spacer(modifier = Modifier.height(height = 15.dp))

        Text(
            text = stringResource(R.string.welcome_screen_subheadline_text),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.padding(horizontal = 45.dp)
        )

        Spacer(modifier = Modifier.height(height = 25.dp))

        when (uiState) {
            UIState.LOADING -> {
                HireTopCircularProgressIndicator()
            }

            UIState.FAILURE -> {
                if (isNetworkAvailable == null || isNetworkAvailable == false) {
                    onNavigateToNextScreen(navController, NetworkIssueScreen.route)
                } else if (isEnterpriseAccount == null) {
                    Button(
                        modifier = Modifier
                            .width(width = mWidth * 0.6F)
                            .height(50.dp)
                            .padding(horizontal = 5.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.onPrimary,
                            contentColor = MaterialTheme.colorScheme.primary
                        ),
                        shape = MaterialTheme.shapes.small,
                        onClick = {
                            onNavigateToNextScreen(
                                navController = navController,
                                destination = LoginScreen.route
                            )
                        }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = stringResource(R.string.let_get_started_text),
                                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp)
                            )

                            Spacer(modifier = Modifier.width(width = 15.dp))

                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowRightAlt,
                                contentDescription = stringResource(R.string.started_button_icon_des),
                                modifier = Modifier.size(size = 35.dp)
                            )
                        }
                    }
                }
            }

            UIState.SUCCESS -> {
                if (isEnterpriseAccount == true) {
                    onNavigateToNextScreen(navController, EnterpriseBottomNavGraph.route)
                } else {
                    onNavigateToNextScreen(navController, CandidateBottomNavGraph.route)
                }
            }
        }

        Spacer(modifier = Modifier.weight(weight = 1F))
    }
}

private fun onNavigateToNextScreen(navController: NavHostController, destination: String) {
    navController.navigate(route = destination) {
        popUpTo(route = WelcomeScreen.route) {
            inclusive = true
        }
    }
}
