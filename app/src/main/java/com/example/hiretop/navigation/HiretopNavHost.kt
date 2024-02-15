package com.example.hiretop.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.hiretop.ui.screens.AccountTypeScreen
import com.example.hiretop.ui.screens.LoginScreen
import com.example.hiretop.ui.screens.SignupScreen
import com.example.hiretop.ui.screens.WelcomeScreen

@Composable
fun HireTopNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = WelcomeScreen.route,
        modifier = modifier
    ) {
        composable(route = WelcomeScreen.route) {
            WelcomeScreen(navController = navController)
        }

        composable(route = LoginScreen.route) {
            LoginScreen(navController = navController)
        }

        composable(route = SignupScreen.route) {
            SignupScreen(navController = navController)
        }

        composable(route = AccountTypeScreen.route) {
            AccountTypeScreen(navController = navController)
        }
    }
}