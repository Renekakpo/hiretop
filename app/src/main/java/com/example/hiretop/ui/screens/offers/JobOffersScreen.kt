package com.example.hiretop.ui.screens.offers

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.hiretop.R
import com.example.hiretop.models.JobOffer
import com.example.hiretop.models.UIState
import com.example.hiretop.ui.extras.HireTopBottomSheet
import com.example.hiretop.ui.extras.HireTopCircularProgressIndicator
import com.example.hiretop.utils.Utils.getPostedTimeAgo
import com.example.hiretop.viewModels.CandidateViewModel
import com.google.gson.Gson

@Composable
fun JobOffersScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    candidateViewModel: CandidateViewModel = hiltViewModel()
) {
    val mContext = LocalContext.current

    // Observe candidate profile and recommended jobs states
    val candidateProfile by candidateViewModel.candidateProfile.collectAsState(null)
    val jobOffers by candidateViewModel.jobOffers.collectAsState(null)
    var uiState by remember { mutableStateOf(UIState.LOADING) }

    var searchInput by remember { mutableStateOf("") }
    var filteredJobOffers by remember { mutableStateOf(jobOffers) }

    var sheetTitle by remember { mutableStateOf("") }
    var showBottomSheet by remember { mutableStateOf(false) }
    var bottomSheetContent by remember { mutableStateOf<@Composable (() -> Unit)?>(null) }

    if (showBottomSheet && bottomSheetContent != null) {
        HireTopBottomSheet(
            title = sheetTitle,
            onDismiss = { showBottomSheet = false }) {
            bottomSheetContent?.invoke()
        }
    }

    LaunchedEffect(Unit) {
        uiState = if (candidateProfile == null) {
            UIState.FAILURE
        } else {
            if (candidateProfile?.skills.isNullOrEmpty()) {
                UIState.FAILURE
            } else {
                candidateViewModel.getAllRelevantJobs(candidateProfile?.skills?.toList().orEmpty())
                UIState.SUCCESS
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(top = 15.dp, start = 15.dp, end = 15.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = searchInput,
                onValueChange = {
                    searchInput = it
                    if (searchInput.length >= 3) {
                        filteredJobOffers = jobOffers?.filter { jobOffer ->
                            jobOffer.title.lowercase()
                                .contains(searchInput.lowercase(), ignoreCase = true)
                        }
                    }
                },
                enabled = !jobOffers.isNullOrEmpty(),
                textStyle = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp),
                placeholder = {
                    Text(
                        text = stringResource(R.string.job_search_field_placeholder_text),
                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp),
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Search,
                        contentDescription = stringResource(R.string.search_offer_icon_desc_text),
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(3.dp)
                    )
                },
                shape = MaterialTheme.shapes.medium,
                singleLine = true,
                maxLines = 1,
                modifier = Modifier
                    .weight(1f)
                    .height(height = 60.dp)
            )

            Box(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(5.dp)
                    .weight(0.2f)
                    .background(
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(corner = CornerSize(8.dp))
                    )
                    .clickable {
                        if (!jobOffers.isNullOrEmpty()) {
                            sheetTitle = mContext.getString(R.string.filter_sheet_title_text)
                            bottomSheetContent = {
                                FilterSheetContent(
                                    onApplyFilter = { jobTypes, educations, locationTypes ->
                                        filteredJobOffers = jobOffers?.filter { jobOffer ->
                                            // Apply filter based on job types, educations, location types
                                            jobTypes.isEmpty() || jobOffer.jobType in jobTypes &&
                                                    educations.isEmpty() || jobOffer.education.any { it in educations } &&
                                                    locationTypes.isEmpty() || jobOffer.locationType in locationTypes
                                        }
                                        showBottomSheet = false
                                    },
                                    onResetFilter = {
                                        filteredJobOffers = jobOffers
                                        showBottomSheet = false
                                    }
                                )
                            }
                            showBottomSheet = true
                        }
                    }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_filter_black_icon),
                    contentDescription = stringResource(R.string.edit_icon_desc_text),
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(size = 45.dp)
                        .padding(5.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(height = 15.dp))

        Divider()

        Spacer(modifier = Modifier.height(height = 15.dp))

        when (uiState) {
            UIState.LOADING -> {
                // Display loader while fetching data
                HireTopCircularProgressIndicator()
            }

            UIState.FAILURE -> {
                if (candidateProfile == null) {
                    // Display text prompting user to complete profile
                    Text(
                        text = stringResource(R.string.update_profile_info),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        textAlign = TextAlign.Center
                    )
                } else if (candidateProfile?.skills.isNullOrEmpty()) {
                    // Display text prompting user to complete profile skills
                    Text(
                        text = stringResource(R.string.incomplet_profile_text),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        textAlign = TextAlign.Center
                    )
                } else {
                    // No job offers found
                    Text(
                        text = stringResource(R.string.no_job_offers_found_text),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }

            UIState.SUCCESS -> {
                filteredJobOffers?.let {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(15.dp)) {
                        itemsIndexed(it) { _, item ->
                            JobOfferItemRow(
                                context = mContext,
                                jobOffer = item,
                                onJobOfferClicked = { onJobOfferClicked(navController, it) })
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun JobOfferItemRow(
    context: Context,
    jobOffer: JobOffer,
    onJobOfferClicked: (JobOffer) -> Unit
) {
    Column(
        modifier = Modifier
            .wrapContentSize()
            .clickable { onJobOfferClicked(jobOffer) }
            .background(
                color = MaterialTheme.colorScheme.background
            )
            .border(
                border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.onBackground),
                shape = RoundedCornerShape(CornerSize(8.dp))
            )
            .padding(15.dp)
    ) {
        Text(
            text = jobOffer.title,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = jobOffer.company ?: "N/A",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(5.dp))

        Text(
            text = jobOffer.locationType ?: "N/A",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(15.dp))

        Text(
            text = jobOffer.description ?: "N/A",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
            maxLines = 4,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = getPostedTimeAgo(context, jobOffer.postedAt),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            maxLines = 4,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))
    }
}

private fun onJobOfferClicked(navController: NavController, jobOffer: JobOffer) {
    navController.navigate(route = "${JobOfferDetailsScreen.route}/${Gson().toJson(jobOffer)}/${false}")
}