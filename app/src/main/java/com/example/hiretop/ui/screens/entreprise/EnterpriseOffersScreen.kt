package com.example.hiretop.ui.screens.entreprise

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Visibility
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
import com.example.hiretop.ui.extras.FailurePopup
import com.example.hiretop.ui.extras.HireTopBottomSheet
import com.example.hiretop.ui.extras.HireTopCircularProgressIndicator
import com.example.hiretop.ui.screens.offers.JobOfferDetailsScreen
import com.example.hiretop.utils.Utils
import com.example.hiretop.viewModels.EnterpriseViewModel
import com.google.gson.Gson

@Composable
fun EnterpriseOffersScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    enterpriseViewModel: EnterpriseViewModel = hiltViewModel()
) {
    val mContext = LocalContext.current

    // Observe candidate profile and recommended jobs states
    val enterpriseProfileId by enterpriseViewModel.enterpriseProfileId.collectAsState(initial = null)
    val jobOffers by enterpriseViewModel.jobOffersList.collectAsState(null)
    var uiState by remember { mutableStateOf(UIState.LOADING) }
    var onErrorMessage by remember { mutableStateOf<String?>(null) }

    var searchInput by remember { mutableStateOf("") }
    var filteredJobOffers by remember { mutableStateOf(jobOffers ?: emptyList()) }

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

    if (!onErrorMessage.isNullOrEmpty()) {
        FailurePopup(errorMessage = "$onErrorMessage", onDismiss = {
            onErrorMessage = null
        })
    }

    LaunchedEffect(enterpriseViewModel) {
        if (enterpriseProfileId == null) {
            uiState = UIState.FAILURE
        } else {
            enterpriseViewModel.getAllJobOffersForEnterprise(
                enterpriseID = "$enterpriseProfileId",
                onSuccess = {
                    uiState = if (it.isEmpty()) {
                        UIState.FAILURE
                    } else {
                        filteredJobOffers = it
                        UIState.SUCCESS
                    }
                },
                onFailure = {
                    onErrorMessage = it
                    uiState = UIState.FAILURE
                }
            )
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(top = 15.dp, start = 15.dp, end = 15.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(R.string.job_offer_management_text),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )

            Icon(
                imageVector = Icons.Outlined.AddCircle,
                contentDescription = stringResource(R.string.search_offer_icon_desc_text),
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(45.dp)
                    .padding(3.dp)
                    .clickable {
                        sheetTitle = mContext.getString(R.string.create_job_offer_text)
                        bottomSheetContent = {
                            CreateOrEditJobOfferScreen(
                                isEditing = false,
                                jobOffer = null,
                                onCancelClicked = {
                                    showBottomSheet = false
                                },
                                onSaveClicked = {
                                    showBottomSheet = false

                                    enterpriseViewModel.getAllJobOffersForEnterprise(
                                        enterpriseID = "$enterpriseProfileId",
                                        onSuccess = {
                                            uiState = if (it.isEmpty()) {
                                                UIState.FAILURE
                                            } else {
                                                filteredJobOffers = it
                                                UIState.SUCCESS
                                            }
                                        },
                                        onFailure = {
                                            onErrorMessage = it
                                            uiState = UIState.FAILURE
                                        }
                                    )
                                },
                                onCloseClicked = { showBottomSheet = false }
                            )
                        }
                        showBottomSheet = true
                    }
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Divider()

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = searchInput,
            onValueChange = {
                searchInput = it
                if (searchInput.isEmpty()) {
                    filteredJobOffers = jobOffers ?: emptyList()
                } else {
                    if (searchInput.length >= 3) {
                        filteredJobOffers = jobOffers?.filter { jobOffer ->
                            "${jobOffer.title}".lowercase()
                                .contains(searchInput.lowercase(), ignoreCase = true)
                        } ?: emptyList()
                    }
                }
            },
            textStyle = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp),
            placeholder = {
                Text(
                    text = stringResource(R.string.role_position_info),
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
            shape = MaterialTheme.shapes.extraLarge,
            singleLine = true,
            maxLines = 1,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        when (uiState) {
            UIState.LOADING -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    // Display loader while fetching data
                    HireTopCircularProgressIndicator()
                }
            }

            UIState.FAILURE -> {
                val failureText = if (enterpriseProfileId == null) {
                    // Display text prompting user to complete profile
                    stringResource(R.string.update_profile_info)
                } else {
                    // No job offers found
                    stringResource(R.string.no_enterprise_job_offer_found)
                }

                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = failureText,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }

            UIState.SUCCESS -> {
                if (filteredJobOffers.isNotEmpty()) {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(15.dp)) {
                        itemsIndexed(filteredJobOffers.sortedByDescending { it.postedAt }) { _, filteredJob ->
                            OfferItemRow(
                                context = mContext,
                                jobOffer = filteredJob,
                                onJobOfferClicked = {
                                    onEnterpriseJobOfferClicked(navController, it)
                                }
                            )
                        }
                    }
                } else {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = stringResource(R.string.no_enterprise_job_offer_found),
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun OfferItemRow(
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
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = jobOffer.title ?: "",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.weight(0.1f))

            if (jobOffer.viewCount > 0) {
                /*Badge(
                    modifier = Modifier.size(33.dp),
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ) {
                    Text(
                        text = if (jobOffer.viewCount > 99) "+99" else "${jobOffer.viewCount}",
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.align(alignment = Alignment.CenterVertically)
                    )
                }
                */
                Row(
                    modifier = Modifier.wrapContentSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Visibility,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = if (jobOffer.viewCount > 99) "+99" else "${jobOffer.viewCount}",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.align(alignment = Alignment.CenterVertically)
                    )
                }
            }
        }

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

        if (jobOffer.postedAt != null) {
            Text(
                text = Utils.getPostedTimeAgo(context, jobOffer.postedAt),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                maxLines = 4,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(10.dp))
    }
}

private fun onEnterpriseJobOfferClicked(navController: NavController, jobOffer: JobOffer) {
    val jobOfferJSON: String = Gson().toJson(jobOffer)
    val isEditable = true
    navController.navigate(route = "${JobOfferDetailsScreen.route}/$jobOfferJSON/$isEditable")
}