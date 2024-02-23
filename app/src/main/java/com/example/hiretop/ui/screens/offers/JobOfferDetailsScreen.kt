package com.example.hiretop.ui.screens.offers

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.School
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.hiretop.R
import com.example.hiretop.models.JobOffer
import com.example.hiretop.navigation.NavDestination
import com.example.hiretop.ui.extras.FailurePopup
import com.example.hiretop.ui.extras.HireTopBottomSheet
import com.example.hiretop.ui.screens.entreprise.CreateOrEditJobOfferScreen
import com.example.hiretop.viewModels.CandidateViewModel

object JobOfferDetailsScreen : NavDestination {
    override val route: String = "job_offer_details_screen"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobOfferDetailsScreen(
    navController: NavController,
    jobOffer: JobOffer,
    isEditable: Boolean,
    candidateViewModel: CandidateViewModel = hiltViewModel()
) {
    val mContext = LocalContext.current
    val mWidth = LocalConfiguration.current.screenWidthDp.dp

    val candidateId by candidateViewModel.candidateProfileId.collectAsState(initial = null)
    val canApplyToJobOffer by candidateViewModel.canApplyToJobOffer.collectAsState()
    var onErrorMessage by remember { mutableStateOf<String?>(null) }
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
            onErrorMessage = null // Reset error message the user have seen it
        })
    }

    LaunchedEffect(candidateViewModel) {
        if (!isEditable && candidateId.toString().isNotEmpty()) {
            candidateViewModel.canApplyToJobOffer(
                jobOfferId = "${jobOffer.jobOfferID}",
                onFailure = { errorMessage ->
                    onErrorMessage = errorMessage
                }
            )
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.wrapContentSize(),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
                navigationIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                        contentDescription = stringResource(id = R.string.back_arrow_icon_desc_text),
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.clickable { navController.navigateUp() }
                    )
                },
                actions = {
                    if (isEditable) {
                        Icon(
                            imageVector = Icons.Outlined.Edit,
                            contentDescription = stringResource(R.string.edit_icon_desc_text),
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier
                                .size(34.dp)
                                .padding(3.dp)
                                .clickable {
                                    sheetTitle = mContext.getString(R.string.optional_about_text)
                                    bottomSheetContent = {
                                        CreateOrEditJobOfferScreen(
                                            isEditing = true,
                                            jobOffer = jobOffer,
                                            onSaveClicked = {
                                                showBottomSheet = false
                                            },
                                            onCancelClicked = {
                                                showBottomSheet = false
                                            },
                                            onCloseClicked = {
                                                showBottomSheet = false
                                            },
                                        )
                                    }
                                    showBottomSheet = true
                                }
                        )
                    }
                },
                title = {
                    Text(
                        text = stringResource(R.string.job_offer_details_title_text),
                        style = MaterialTheme.typography.titleLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background)
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
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

            Spacer(modifier = Modifier.height(15.dp))

            Text(
                text = jobOffer.company ?: "",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = jobOffer.locationType ?: "",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )

            if (!isEditable) {
                Spacer(modifier = Modifier.height(15.dp))

                Button(
                    modifier = Modifier
                        .width(width = mWidth * 0.4F)
                        .height(45.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    shape = MaterialTheme.shapes.small,
                    enabled = canApplyToJobOffer,
                    onClick = {
                        candidateViewModel.applyToJobOffer(
                            jobOffer = jobOffer,
                            onSuccess = {
                                candidateViewModel.updateCanApplyToJobOffer(false)
                            },
                            onError = { errorMessage ->
                                // Show a pop-up or error message to inform the user about the failure
                                onErrorMessage = errorMessage
                            }
                        )
                    }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = stringResource(R.string.apply_text),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Divider()

            Spacer(modifier = Modifier.height(15.dp))

            Text(
                text = stringResource(R.string.profile_insights_text),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = stringResource(R.string.profile_insights_info_text),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(15.dp))

            Row(verticalAlignment = Alignment.Top) {
                Icon(
                    imageVector = Icons.Outlined.AutoAwesome,
                    contentDescription = stringResource(R.string.bookmark_offer_icon_desc),
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .size(34.dp)
                        .padding(3.dp)
                        .weight(0.1f)
                )

                Spacer(modifier = Modifier.width(15.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(id = R.string.optional_skills_text),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    LazyRow(
                        state = rememberLazyListState(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        itemsIndexed(jobOffer.skills) { _, item ->
                            JobSkillOrEducationItemRow(item = item)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(verticalAlignment = Alignment.Top) {
                Icon(
                    imageVector = Icons.Outlined.School,
                    contentDescription = stringResource(R.string.education_offer_icon_desc),
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .size(34.dp)
                        .padding(3.dp)
                        .weight(0.1f)
                )

                Spacer(modifier = Modifier.width(15.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(id = R.string.education_text),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    LazyRow(
                        state = rememberLazyListState(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        itemsIndexed(jobOffer.education) { _, item ->
                            JobSkillOrEducationItemRow(item = item)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(25.dp))

            Divider()

            Spacer(modifier = Modifier.height(15.dp))

            Text(
                text = stringResource(R.string.job_info_text),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(15.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${stringResource(R.string.location_type_text)}: ",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.wrapContentSize()
                )

                Text(
                    text = jobOffer.locationType ?: "",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(15.dp))

            Text(
                text = jobOffer.description ?: "",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.wrapContentSize()
            )

            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
fun JobSkillOrEducationItemRow(item: String) {
    Box(
        modifier = Modifier
            .wrapContentSize()
            .background(
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                shape = RoundedCornerShape(CornerSize(8.dp))
            )
            .padding(horizontal = 14.dp, vertical = 7.dp),
    ) {
        Text(
            text = item,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .fillMaxWidth()
                .align(alignment = Alignment.Center)
        )
    }
}