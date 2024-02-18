package com.example.hiretop.helpers

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.hiretop.models.generateFakeJobOffers
import com.example.hiretop.ui.screens.AccountTypeScreen
import com.example.hiretop.ui.screens.LoginScreen
import com.example.hiretop.ui.screens.SignupScreen
import com.example.hiretop.ui.screens.WelcomeScreen
import com.example.hiretop.ui.screens.offers.JobOfferDetailsScreen
import com.example.hiretop.ui.screens.offers.JobOfferItemRow
import com.example.hiretop.ui.screens.offers.JobOffersScreen
import com.example.hiretop.ui.screens.profile.EditOrAddCertificationSection
import com.example.hiretop.ui.screens.profile.EditOrAddEducationSection
import com.example.hiretop.ui.screens.profile.EditOrAddExperienceSection
import com.example.hiretop.ui.screens.profile.EditOrAddProjectSection
import com.example.hiretop.ui.screens.profile.EditOrAddSkillSection
import com.example.hiretop.ui.screens.profile.EditProfileAboutSection
import com.example.hiretop.ui.screens.profile.EditProfileHeaderSection
import com.example.hiretop.ui.screens.profile.TalentProfileScreen
import com.example.hiretop.ui.theme.HiretopTheme


/*@Preview
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
        EditProfileHeaderSection()
    }
}

@Preview
@Composable
fun EditProfileAboutSectionPreview() {
    HiretopTheme {
        EditProfileAboutSection()
    }
}

@Preview
@Composable
fun EditOrAddExperienceSectionPreview() {
    HiretopTheme {
        EditOrAddExperienceSection()
    }
}

@Preview
@Composable
fun EditOrAddEducationSectionPreview() {
    HiretopTheme {
        EditOrAddEducationSection()
    }
}

@Preview
@Composable
fun EditOrAddCertificationSectionPreview() {
    HiretopTheme {
        EditOrAddCertificationSection()
    }
}

@Preview
@Composable
fun EditOrAddProjectSectionPreview() {
    HiretopTheme {
        EditOrAddProjectSection()
    }
}

@Preview
@Composable
fun EditOrAddSkillSectionPreview() {
    HiretopTheme {
        EditOrAddSkillSection()
    }
}

@Preview
@Composable
fun JobOffersScreenPreview() {
    HiretopTheme {
        JobOffersScreen()
    }
}*/

@Preview
@Composable
fun JobOfferDetailsScreenPreview() {
    HiretopTheme {
        val jobOffer = generateFakeJobOffers(10).first()
        JobOfferDetailsScreen(jobOffer)
    }
}