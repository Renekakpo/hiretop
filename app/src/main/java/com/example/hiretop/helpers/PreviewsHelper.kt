package com.example.hiretop.helpers

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.hiretop.models.generateFakeJobOffers
import com.example.hiretop.ui.screens.AccountTypeScreen
import com.example.hiretop.ui.screens.WelcomeScreen
import com.example.hiretop.ui.screens.auth.LoginScreen
import com.example.hiretop.ui.screens.auth.SignupScreen
import com.example.hiretop.ui.screens.candidate.dashboard.TalentDashboardScreen
import com.example.hiretop.ui.screens.candidate.profile.EditOrAddCertificationSection
import com.example.hiretop.ui.screens.candidate.profile.EditOrAddEducationSection
import com.example.hiretop.ui.screens.candidate.profile.EditOrAddExperienceSection
import com.example.hiretop.ui.screens.candidate.profile.EditOrAddProjectSection
import com.example.hiretop.ui.screens.candidate.profile.EditOrAddSkillSection
import com.example.hiretop.ui.screens.candidate.profile.EditProfileAboutSection
import com.example.hiretop.ui.screens.candidate.profile.TalentProfileScreen
import com.example.hiretop.ui.screens.candidate.tracking.JobApplicationsScreen
import com.example.hiretop.ui.screens.entreprise.CreateOrEditJobOfferScreen
import com.example.hiretop.ui.screens.entreprise.EnterpriseOffersScreen
import com.example.hiretop.ui.screens.entreprise.applications.EditApplicationsDetailsScreen
import com.example.hiretop.ui.screens.entreprise.applications.EnterpriseApplicationsScreen
import com.example.hiretop.ui.screens.entreprise.dashboard.EnterpriseDashboardScreen
import com.example.hiretop.ui.screens.entreprise.presentation.EditProfileHeaderSection
import com.example.hiretop.ui.screens.entreprise.presentation.EnterpriseProfileScreen
import com.example.hiretop.ui.screens.messaging.CandidateInteractionScreen
import com.example.hiretop.ui.screens.messaging.ChatListScreen
import com.example.hiretop.ui.screens.offers.JobOfferDetailsScreen
import com.example.hiretop.ui.screens.offers.JobOffersScreen
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

@Preview
@Composable
fun TalentProfileScreenPreview() {
    HiretopTheme {
        TalentProfileScreen()
    }
}

@Preview
@Composable
fun EditProfileHeaderSectionPreview() {
    HiretopTheme {
        EditProfileHeaderSection(onSaveClicked = {})
    }
}

@Preview
@Composable
fun EditProfileAboutSectionPreview() {
    HiretopTheme {
        EditProfileAboutSection(onSaveClicked = {})
    }
}

@Preview
@Composable
fun EditOrAddExperienceSectionPreview() {
    HiretopTheme {
        EditOrAddExperienceSection(onSaveClicked = {})
    }
}

@Preview
@Composable
fun EditOrAddEducationSectionPreview() {
    HiretopTheme {
        EditOrAddEducationSection(onSaveClicked = {})
    }
}

@Preview
@Composable
fun EditOrAddCertificationSectionPreview() {
    HiretopTheme {
        EditOrAddCertificationSection{}
    }
}

@Preview
@Composable
fun EditOrAddProjectSectionPreview() {
    HiretopTheme {
        EditOrAddProjectSection{}
    }
}

@Preview
@Composable
fun EditOrAddSkillSectionPreview() {
    HiretopTheme {
        EditOrAddSkillSection{}
    }
}

@Preview
@Composable
fun JobOffersScreenPreview() {
    HiretopTheme {
        JobOffersScreen()
    }
}

@Preview
@Composable
fun JobOfferDetailsScreenPreview() {
    HiretopTheme {
        val jobOffer = generateFakeJobOffers(1).first()
        JobOfferDetailsScreen(jobOffer)
    }
}

@Preview
@Composable
fun JobApplicationsScreenPreview() {
    HiretopTheme {
        JobApplicationsScreen()
    }
}

@Preview
@Composable
fun StatisticsAndRecommendationScreenPreview() {
    HiretopTheme {
        TalentDashboardScreen()
    }
}

@Preview
@Composable
fun EnterprisePresentationScreenPreview() {
    HiretopTheme {
        EnterpriseProfileScreen()
    }
}

@Preview
@Composable
fun JobOfferManagementScreenPreview() {
    HiretopTheme {
        CreateOrEditJobOfferScreen()
    }
}

@Preview
@Composable
fun EnterpriseOffersScreenPreview() {
    HiretopTheme {
        EnterpriseOffersScreen()
    }
}

@Preview
@Composable
fun EnterpriseApplicationsScreenPreview() {
    HiretopTheme {
        EnterpriseApplicationsScreen()
    }
}

@Preview
@Composable
fun CandidateInteractionScreenPreview() {
    HiretopTheme {
        CandidateInteractionScreen()
    }
}

@Preview
@Composable
fun ChatListScreenPreview() {
    HiretopTheme {
        ChatListScreen()
    }
}

@Preview
@Composable
fun EnterpriseDashboardScreenPreview() {
    HiretopTheme {
        EnterpriseDashboardScreen()
    }
}

@Preview
@Composable
fun ApplicationsDetailsScreenPreview() {
    HiretopTheme {
        EditApplicationsDetailsScreen()
    }
}