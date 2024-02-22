package com.example.hiretop.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.hiretop.models.generateFakeJobOffers
import com.example.hiretop.ui.screens.AccountTypeScreen
import com.example.hiretop.ui.screens.auth.LoginScreen
import com.example.hiretop.ui.screens.auth.SignupScreen
import com.example.hiretop.ui.screens.WelcomeScreen
import com.example.hiretop.ui.screens.candidate.profile.EditHeaderSection
import com.example.hiretop.ui.screens.candidate.profile.EditOrAddCertificationSection
import com.example.hiretop.ui.screens.candidate.profile.EditOrAddEducationSection
import com.example.hiretop.ui.screens.candidate.profile.EditOrAddExperienceSection
import com.example.hiretop.ui.screens.candidate.profile.EditOrAddProjectSection
import com.example.hiretop.ui.screens.candidate.profile.EditOrAddSkillSection
import com.example.hiretop.ui.screens.candidate.profile.EditProfileAboutSection
import com.example.hiretop.ui.screens.entreprise.CreateOrEditJobOfferScreen
import com.example.hiretop.ui.screens.entreprise.applications.EditApplicationsDetailsScreen
import com.example.hiretop.ui.screens.entreprise.applications.EnterpriseApplicationsScreen
import com.example.hiretop.ui.screens.entreprise.presentation.EditEnterpriseAboutSection
import com.example.hiretop.ui.screens.entreprise.presentation.EditEnterpriseContactDetailsSection
import com.example.hiretop.ui.screens.entreprise.presentation.EditEnterpriseCultureAndValuesSection
import com.example.hiretop.ui.screens.messaging.CandidateInteractionScreen
import com.example.hiretop.ui.screens.offers.JobOfferDetailsScreen

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


        composable(route = EditHeaderSection.route) {
            EditHeaderSection { }
        }

        composable(route = EditHeaderSection.route) {
            EditOrAddCertificationSection { }
        }

        composable(route = EditHeaderSection.route) {
            EditOrAddEducationSection { }
        }

        composable(route = EditHeaderSection.route) {
            EditOrAddExperienceSection { }
        }

        composable(route = EditHeaderSection.route) {
            EditOrAddProjectSection { }
        }

        composable(route = EditHeaderSection.route) {
            EditOrAddSkillSection { }
        }

        composable(route = EditHeaderSection.route) {
            EditProfileAboutSection() { }
        }

        composable(route = EditHeaderSection.route) {
            EditApplicationsDetailsScreen()
        }

        composable(route = EditHeaderSection.route) {
            EnterpriseApplicationsScreen()
        }

        composable(route = EditHeaderSection.route) {
            EditEnterpriseAboutSection { }
        }

        composable(route = EditHeaderSection.route) {
            EditEnterpriseContactDetailsSection { }
        }

        composable(route = EditHeaderSection.route) {
            EditEnterpriseCultureAndValuesSection { }
        }

        composable(route = EditHeaderSection.route) {
            CreateOrEditJobOfferScreen()
        }

        composable(route = EditHeaderSection.route) {
            CandidateInteractionScreen()
        }

        composable(route = EditHeaderSection.route) {
            JobOfferDetailsScreen(jobOffer = generateFakeJobOffers(1).first())
        }
    }
}