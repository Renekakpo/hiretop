package com.example.hiretop.ui.screens.candidate.dashboard

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.BusinessCenter
import androidx.compose.material.icons.outlined.Grade
import androidx.compose.material.icons.outlined.School
import androidx.compose.material.icons.outlined.WorkOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.hiretop.R
import com.example.hiretop.models.CandidateProfile
import com.example.hiretop.models.JobOffer
import com.example.hiretop.models.UIState
import com.example.hiretop.ui.extras.HireTopCircularProgressIndicator
import com.example.hiretop.ui.screens.offers.JobOfferDetailsScreen
import com.example.hiretop.utils.Utils
import com.example.hiretop.viewModels.CandidateViewModel

@Composable
fun CandidateDashboardScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    candidateViewModel: CandidateViewModel = hiltViewModel()
) {
    val mContext = LocalContext.current

    // Observe candidate profile and recommended jobs states
    val candidateProfileId by candidateViewModel.candidateProfileId.collectAsState(null)
    val candidateProfile by candidateViewModel.candidateProfile.collectAsState(null)
    val recommendedJobs by candidateViewModel.recommendedJobs.collectAsState(null)
    var uiState by remember { mutableStateOf(UIState.LOADING) }

    LaunchedEffect(candidateViewModel) {
        if (candidateProfileId == null) {

        } else {
            candidateViewModel.getCandidateProfile(profileId = "$candidateProfileId") { profile ->
                uiState = if (profile == null) {
                    UIState.FAILURE
                } else {
                    if (!profile.skills.isNullOrEmpty()) {
                        candidateViewModel.getRecommendedJobs(profile.skills)
                    }
                    UIState.SUCCESS
                }
            }
        }
    }

    when (uiState) {
        UIState.LOADING -> {
            // Display loader while fetching data
            HireTopCircularProgressIndicator()
        }

        UIState.FAILURE -> {
            // Display text prompting user to complete profile
            Text(
                text = stringResource(R.string.complete_profile_info),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                textAlign = TextAlign.Center
            )
        }

        UIState.SUCCESS -> {
            // Display dashboard content and recommended jobs
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.background)
                    .padding(top = 15.dp, start = 15.dp, end = 15.dp)
            ) {
                Text(
                    text = stringResource(R.string.statistics_text),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Divider()

                Spacer(modifier = Modifier.height(15.dp))

                candidateProfile?.let { profile ->
                    Statistics(candidateProfile = profile)
                }

                Spacer(modifier = Modifier.height(25.dp))

                Text(
                    text = stringResource(R.string.recommendations_text),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Divider()

                Spacer(modifier = Modifier.height(15.dp))

                recommendedJobs?.let { jobs ->
                    if (candidateProfile?.skills.isNullOrEmpty()) {
                        // No skills defined on profile
                        Text(
                            text = stringResource(R.string.no_skills_defined_on_profile),
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(15.dp)
                        )
                    } else if (jobs.isEmpty()) {
                        // No recommendations found
                        Text(
                            text = stringResource(R.string.no_job_recommendations_found),
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(16.dp)
                        )
                    } else {
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(15.dp)) {
                            itemsIndexed(jobs) { _, jobOffer ->
                                RecommendationsItemRow(
                                    context = mContext,
                                    jobOffer = jobOffer,
                                    onJobOfferClicked = {
                                        TODO("Navigate to JobOfferDetails screen with jobOffer as param")
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Statistics(candidateProfile: CandidateProfile) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.inversePrimary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            ProfileStatistic(
                icon = Icons.Outlined.WorkOutline,
                title = stringResource(id = R.string.experience_text),
                value = "${candidateProfile.experiences?.size ?: 0}"
            )

            Spacer(modifier = Modifier.height(15.dp))

            ProfileStatistic(
                icon = Icons.Outlined.AutoAwesome,
                title = stringResource(id = R.string.optional_skills_text),
                value = "${candidateProfile.skills?.size ?: 0}"
            )

            Spacer(modifier = Modifier.height(15.dp))

            ProfileStatistic(
                icon = Icons.Outlined.BusinessCenter,
                title = stringResource(id = R.string.projects_text),
                value = "${candidateProfile.projects?.size ?: 0}"
            )

            Spacer(modifier = Modifier.height(15.dp))

            ProfileStatistic(
                icon = Icons.Outlined.School,
                title = stringResource(id = R.string.education_text),
                value = "${candidateProfile.education?.size}"
            )

            Spacer(modifier = Modifier.height(15.dp))

            ProfileStatistic(
                icon = Icons.Outlined.Grade,
                title = stringResource(id = R.string.certifications_text),
                value = "${candidateProfile.certifications?.size}"
            )
        }
    }
}

@Composable
fun ProfileStatistic(icon: ImageVector, title: String, value: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.onPrimary,
                    shape = CircleShape
                )
                .padding(4.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
        )

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@Composable
fun RecommendationsItemRow(
    context: Context,
    jobOffer: JobOffer,
    onJobOfferClicked: (JobOffer) -> Unit
) {
    Column(
        modifier = Modifier
            .wrapContentSize()
            .background(
                color = MaterialTheme.colorScheme.background
            )
            .border(
                border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.onBackground),
                shape = RoundedCornerShape(CornerSize(8.dp))
            )
            .padding(15.dp)
            .clickable { onJobOfferClicked(jobOffer) }
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = jobOffer.title,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )

            /*Spacer(modifier = Modifier.weight(0.1f))

            Icon(
                imageVector = Icons.Outlined.BookmarkBorder,
                contentDescription = stringResource(R.string.bookmark_offer_icon_desc),
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .size(34.dp)
                    .padding(3.dp)
                    .clickable { onBookmarkOfferClicked() }
            )*/
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = jobOffer.company,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(5.dp))

        Text(
            text = jobOffer.locationType,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(15.dp))

        Text(
            text = jobOffer.description,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
            maxLines = 4,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = Utils.getPostedTimeAgo(context, jobOffer.postedAt),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            maxLines = 4,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

private fun onBookmarkOfferClicked() {
    TODO("Not yet implemented")
}