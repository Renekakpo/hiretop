package com.example.hiretop.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.hiretop.models.JobOffer
import com.example.hiretop.ui.screens.AccountTypeScreen
import com.example.hiretop.ui.screens.WelcomeScreen
import com.example.hiretop.ui.screens.auth.LoginScreen
import com.example.hiretop.ui.screens.auth.SignupScreen
import com.example.hiretop.ui.screens.candidate.profile.EditHeaderSection
import com.example.hiretop.ui.screens.candidate.profile.EditOrAddCertificationSection
import com.example.hiretop.ui.screens.candidate.profile.EditOrAddEducationSection
import com.example.hiretop.ui.screens.candidate.profile.EditOrAddExperienceSection
import com.example.hiretop.ui.screens.candidate.profile.EditOrAddProjectSection
import com.example.hiretop.ui.screens.candidate.profile.EditOrAddSkillSection
import com.example.hiretop.ui.screens.candidate.profile.EditProfileAboutSection
import com.example.hiretop.ui.screens.entreprise.applications.EditApplicationsDetailsScreen
import com.example.hiretop.ui.screens.entreprise.applications.EnterpriseApplicationsScreen
import com.example.hiretop.ui.screens.entreprise.presentation.EditEnterpriseAboutSection
import com.example.hiretop.ui.screens.entreprise.presentation.EditEnterpriseContactDetailsSection
import com.example.hiretop.ui.screens.entreprise.presentation.EditEnterpriseCultureAndValuesSection
import com.example.hiretop.ui.screens.messaging.CandidateInteractionScreen
import com.example.hiretop.ui.screens.offers.JobOfferDetailsScreen
import com.google.gson.Gson

@Composable
fun HireTopNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    accountType: Int
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

        composable(route = EditApplicationsDetailsScreen.route) {
            EditApplicationsDetailsScreen()
        }

        composable(route = EnterpriseApplicationsScreen.route) {
            EnterpriseApplicationsScreen()
        }

        composable(route = CandidateInteractionScreen.route) {
            CandidateInteractionScreen()
        }

        composable(
            route = "${JobOfferDetailsScreen.route}/jobOffer/isEditable",
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
    }
}