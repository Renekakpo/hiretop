package com.example.hiretop.app

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.hiretop.navigation.HireTopNavHost

@Composable
fun HireTopApp(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    HireTopNavHost(
        modifier = modifier,
        navController = navController
    )
}