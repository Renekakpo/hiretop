package com.example.hiretop.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.hiretop.models.ChatItemUI
import com.example.hiretop.models.JobApplication
import com.example.hiretop.models.JobOffer
import com.example.hiretop.ui.extras.NetworkIssueScreen
import com.example.hiretop.ui.screens.AccountTypeScreen
import com.example.hiretop.ui.screens.WelcomeScreen
import com.example.hiretop.ui.screens.auth.LoginScreen
import com.example.hiretop.ui.screens.auth.SignupScreen
import com.example.hiretop.ui.screens.candidate.profile.CandidateProfileScreen
import com.example.hiretop.ui.screens.entreprise.applications.EditApplicationsDetailsScreen
import com.example.hiretop.ui.screens.entreprise.applications.EnterpriseApplicationsScreen
import com.example.hiretop.ui.screens.entreprise.presentation.EnterpriseProfileScreen
import com.example.hiretop.ui.screens.messaging.ChatScreen
import com.example.hiretop.ui.screens.offers.JobOfferDetailsScreen
import com.google.gson.Gson

@Composable
fun HireTopMainNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController
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

        composable(route = CandidateBottomNavGraph.route) {
            TalentBottomNavHost(navController = navController)
        }

        composable(route = EnterpriseBottomNavGraph.route) {
            EnterpriseBottomNavHost(navController = navController)
        }

        composable(route = NetworkIssueScreen.route) {
            NetworkIssueScreen()
        }

        composable(
            route = "${EditApplicationsDetailsScreen.route}/{jobApplication}/{isViewMode}",
            arguments = listOf(
                navArgument(name = "jobApplication") { type = NavType.StringType },
                navArgument(name = "isViewMode") { type = NavType.BoolType }
            )
        ) { backStackEntry ->
            val jobApplicationJSON = backStackEntry.arguments?.getString("jobApplication")
            val isViewMode = backStackEntry.arguments?.getBoolean("isViewMode") ?: false

            if (!jobApplicationJSON.isNullOrEmpty()) {
                val jobApplication = Gson().fromJson(jobApplicationJSON, JobApplication::class.java)
                EditApplicationsDetailsScreen(
                    navController = navController,
                    jobApplication = jobApplication,
                    isPreviewMode = isViewMode
                )
            } else {
                // Navigate back
                navController.popBackStack()
            }
        }

        composable(route = EnterpriseApplicationsScreen.route) {
            EnterpriseApplicationsScreen(navController = navController)
        }

        composable(
            route = "${ChatScreen.route}/{chatItemUIJson}",
            arguments = listOf(
                navArgument(name = "chatItemUIJson") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val chatItemUIJson = backStackEntry.arguments?.getString("chatItemUIJson")
            if (!chatItemUIJson.isNullOrEmpty()) {
                val chatItemUI = Gson().fromJson(chatItemUIJson, ChatItemUI::class.java)
                ChatScreen(navController = navController, chatItemUI = chatItemUI)
            } else {
                // Navigate back
                navController.popBackStack()
            }
        }

        composable(
            route = "${JobOfferDetailsScreen.route}/{jobOffer}/{isEditable}",
            arguments = listOf(
                navArgument(name = "jobOffer") { type = NavType.StringType },
                navArgument(name = "isEditable") { type = NavType.BoolType }
            )
        ) { backStackEntry ->
            val jobOfferJSON = backStackEntry.arguments?.getString("jobOffer")
            val editable = backStackEntry.arguments?.getBoolean("isEditable") ?: false

            if (jobOfferJSON != null) {
                val jobOffer = Gson().fromJson(jobOfferJSON, JobOffer::class.java)
                JobOfferDetailsScreen(
                    navController = navController,
                    jobOffer = jobOffer,
                    isEditable = editable
                )
            } else {
                // Navigate back
                navController.popBackStack()
            }
        }

        composable(
            route = "${CandidateProfileScreen.route}/{isPreviewMode}/{profileId}",
            arguments = listOf(
                navArgument(name = "isPreviewMode") { type = NavType.BoolType },
                navArgument(name = "profileId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val isPreviewMode = backStackEntry.arguments?.getBoolean("isPreviewMode") ?: true
            val profileId = backStackEntry.arguments?.getString("profileId") ?: ""

            CandidateProfileScreen(
                isPreviewMode = isPreviewMode,
                argCandidateProfileId = profileId,
                navController = navController,
            )
        }

        composable(
            route = "${EnterpriseProfileScreen.route}/{isPreviewMode}/{profileId}",
            arguments = listOf(
                navArgument(name = "isPreviewMode") { type = NavType.BoolType },
                navArgument(name = "profileId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val isPreviewMode = backStackEntry.arguments?.getBoolean("isPreviewMode") ?: true
            val profileId = backStackEntry.arguments?.getString("profileId") ?: ""

            EnterpriseProfileScreen(
                isPreviewMode = isPreviewMode,
                argEnterpriseProfileId = profileId,
                navController = navController,
            )
        }
    }
}