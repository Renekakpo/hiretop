package com.example.hiretop.helpers

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.hiretop.ui.screens.AccountTypeScreen
import com.example.hiretop.ui.screens.LoginScreen
import com.example.hiretop.ui.screens.SignupScreen
import com.example.hiretop.ui.screens.WelcomeScreen
import com.example.hiretop.ui.theme.HiretopTheme

@Preview
@Composable
fun WelcomeScreenPreview() {
    HiretopTheme {
        WelcomeScreen(navController = rememberNavController())
    }
}

@Preview
@Composable
fun LoginScreenPreview() {
    HiretopTheme {
        LoginScreen(navController = rememberNavController())
    }
}

@Preview
@Composable
fun SignupScreenPreview() {
    HiretopTheme {
        SignupScreen(navController = rememberNavController())
    }
}

@Preview
@Composable
fun AccountTypeScreenPreview() {
    HiretopTheme {
        AccountTypeScreen(navController = rememberNavController())
    }
}