package com.example.hiretop.app

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.hiretop.navigation.HireTopMainNavHost

@Composable
fun HireTopApp(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    HireTopMainNavHost(
        modifier = modifier,
        navController = navController
    )
}