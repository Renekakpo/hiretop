package com.example.hiretop.helpers

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.hiretop.models.generateFakeJobOffers
import com.example.hiretop.ui.extras.FailurePopup
import com.example.hiretop.ui.extras.HireTopCircularProgressIndicator
import com.example.hiretop.ui.screens.auth.LoginScreen
import com.example.hiretop.ui.screens.auth.SignupScreen
import com.example.hiretop.ui.screens.candidate.dashboard.CandidateDashboardScreen
import com.example.hiretop.ui.screens.candidate.profile.EditOrAddCertificationSection
import com.example.hiretop.ui.screens.candidate.profile.EditOrAddEducationSection
import com.example.hiretop.ui.screens.candidate.profile.EditOrAddExperienceSection
import com.example.hiretop.ui.screens.candidate.profile.EditOrAddProjectSection
import com.example.hiretop.ui.screens.candidate.profile.EditOrAddSkillSection
import com.example.hiretop.ui.screens.candidate.profile.EditProfileAboutSection
import com.example.hiretop.ui.screens.candidate.profile.CandidateProfileScreen
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


/*@Preview
@Composable
fun WelcomeScreenPreview() {
    HiretopTheme {
        WelcomeScreen(navController = rememberNavController())
    }
}*/

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

/*@Preview
@Composable
fun AccountTypeScreenPreview() {
    HiretopTheme {
        AccountTypeScreen(navController = rememberNavController())
    }
}*/

@Preview
@Composable
fun TalentProfileScreenPreview() {
    HiretopTheme {
        CandidateProfileScreen()
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
        EditProfileAboutSection(currentValue = null, onSaveClicked = {})
    }
}

@Preview
@Composable
fun EditOrAddExperienceSectionPreview() {
    HiretopTheme {
        EditOrAddExperienceSection(currentValue = null, onSaveClicked = {})
    }
}

@Preview
@Composable
fun EditOrAddEducationSectionPreview() {
    HiretopTheme {
        EditOrAddEducationSection(currentValue = null, onSaveClicked = {})
    }
}

@Preview
@Composable
fun EditOrAddCertificationSectionPreview() {
    HiretopTheme {
        EditOrAddCertificationSection(currentValue = null, ) {}
    }
}

@Preview
@Composable
fun EditOrAddProjectSectionPreview() {
    HiretopTheme {
        EditOrAddProjectSection(currentValue = null, ) {}
    }
}

@Preview
@Composable
fun EditOrAddSkillSectionPreview() {
    HiretopTheme {
        EditOrAddSkillSection(currentValue = null, ) {}
    }
}

@Preview
@Composable
fun JobOffersScreenPreview() {
    HiretopTheme {
        JobOffersScreen(navController = rememberNavController())
    }
}

@Preview
@Composable
fun JobOfferDetailsScreenPreview() {
    HiretopTheme {
        val jobOffer = generateFakeJobOffers(1).first()
        JobOfferDetailsScreen(
            navController = rememberNavController(),
            jobOffer = jobOffer,
            isEditable = false
        )
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
fun CandidateDashboardScreenScreenPreview() {
    HiretopTheme {
        CandidateDashboardScreen(
            navController = rememberNavController()
        )
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
fun CreateOrEditJobOfferScreenPreview() {
    HiretopTheme {
        CreateOrEditJobOfferScreen(
            isEditing = false,
            jobOffer = generateFakeJobOffers(1).first(),
            onCancelClicked = {},
            onSaveClicked = {},
            onCloseClicked = {},
        )
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

@Preview
@Composable
fun HireTopCircularProgressIndicatorPreview() {
    HiretopTheme {
        HireTopCircularProgressIndicator()
    }
}

@Preview
@Composable
fun FailurePopupPreview() {
    HiretopTheme {
        FailurePopup(
            errorMessage = "Error message goes here.",
            onDismiss= {}
        )
    }
}