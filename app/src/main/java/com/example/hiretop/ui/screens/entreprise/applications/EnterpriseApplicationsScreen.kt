package com.example.hiretop.ui.screens.entreprise.applications

import android.content.Context
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.hiretop.R
import com.example.hiretop.models.ChatItemUI
import com.example.hiretop.models.JobApplication
import com.example.hiretop.models.UIState
import com.example.hiretop.navigation.NavDestination
import com.example.hiretop.ui.extras.FailurePopup
import com.example.hiretop.ui.extras.HireTopCircularProgressIndicator
import com.example.hiretop.ui.screens.candidate.profile.CandidateProfileScreen
import com.example.hiretop.ui.screens.messaging.ChatScreen
import com.example.hiretop.utils.Utils
import com.example.hiretop.viewModels.EnterpriseViewModel
import com.google.gson.Gson
import java.net.URLEncoder

object EnterpriseApplicationsScreen : NavDestination {
    override val route: String = "enterprise_applications_Screen"
}

@Composable
fun EnterpriseApplicationsScreen(
    navController: NavController,
    enterpriseViewModel: EnterpriseViewModel = hiltViewModel()
) {
    val mContext = LocalContext.current

    var searchInput by remember { mutableStateOf("") }

    val enterpriseProfileId by enterpriseViewModel.enterpriseProfileId.collectAsState(initial = null)
    val jobApplicationsList by enterpriseViewModel.jobApplications.collectAsState()
    var filteredJobApplicationsList by remember {
        mutableStateOf(
            jobApplicationsList ?: emptyList()
        )
    }
    var uiState by remember { mutableStateOf(UIState.LOADING) }
    var onErrorMessage by remember { mutableStateOf<String?>(null) }

    if (!onErrorMessage.isNullOrEmpty()) {
        FailurePopup(
            errorMessage = "$onErrorMessage",
            onDismiss = { onErrorMessage = null }
        )
    }

    LaunchedEffect(enterpriseViewModel) {
        if (enterpriseProfileId == null) {
            uiState = UIState.FAILURE
        } else {
            enterpriseViewModel.getJobApplicationsList(
                enterpriseProfileId = "$enterpriseProfileId",
                onSuccess = { applications ->
                    uiState = if (applications.isEmpty()) {
                        UIState.FAILURE
                    } else {
                        filteredJobApplicationsList = applications
                        UIState.SUCCESS
                    }
                },
                onFailure = {
                    onErrorMessage = it
                    UIState.FAILURE
                }
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(15.dp)
    ) {
        Text(
            text = stringResource(R.string.applications_management_text),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth(1f)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Divider()

        Spacer(modifier = Modifier.height(25.dp))

        OutlinedTextField(
            value = searchInput,
            onValueChange = {
                searchInput = it
                if (searchInput.isEmpty()) {
                    filteredJobApplicationsList = jobApplicationsList ?: emptyList()
                } else {
                    if (searchInput.length >= 3) {
                        filteredJobApplicationsList = jobApplicationsList?.filter { item ->
                            "${item.candidateFullName}".lowercase()
                                .contains(searchInput.lowercase(), ignoreCase = true) ||
                                    item.status?.lowercase()?.contains(
                                        searchInput.lowercase(),
                                        ignoreCase = true
                                    ) == true ||
                                    item.stages?.lowercase()?.contains(
                                        searchInput.lowercase(),
                                        ignoreCase = true
                                    ) == true ||
                                    "${item.jobOfferTitle}".lowercase()
                                        .contains(searchInput.lowercase(), ignoreCase = true)
                        } ?: emptyList()
                    }
                }
            },
            textStyle = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp),
            placeholder = {
                Text(
                    text = stringResource(R.string.application_search_field_placeholder_text),
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp),
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Search,
                    contentDescription = stringResource(R.string.application_search_icon_desc_text),
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
                    stringResource(R.string.empty_enterprise_applications_list_info)
                } else {
                    // No job applications found
                    stringResource(R.string.enterprise_no_job_application_found_info)
                }

                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = failureText,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(16.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }

            UIState.SUCCESS -> {
                if (filteredJobApplicationsList.isNotEmpty()) {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(15.dp)) {
                        itemsIndexed(filteredJobApplicationsList) { _, item ->
                            Spacer(modifier = Modifier.height(5.dp))

                            ApplicationItemRow(
                                context = mContext,
                                jobApplication = item,
                                onNavigateToJobApplicationDetailsScreen = {
                                    navigateToApplicationDetails(navController, it)
                                },
                                onPreviewCandidateProfile = {
                                    navigateToCandidateProfile(navController, it)
                                },
                                onChatIconClicked = { jobApplication ->
                                    if (!jobApplication.jobApplicationId.isNullOrEmpty()) {
                                        val onSuccess: (String) -> Unit = { chatId ->
                                            val chatItemUI = ChatItemUI(
                                                chatId = chatId,
                                                pictureUrl = jobApplication.candidatePictureUrl
                                                    ?: "",
                                                profileName = jobApplication.candidateFullName
                                                    ?: "",
                                                offerTitle = jobApplication.jobOfferTitle ?: "",
                                                unreadMessageCount = 0,
                                                jobApplicationId = jobApplication.jobApplicationId
                                            )
                                            navigateToChatScreen(navController, chatItemUI)
                                        }

                                        val onFailure: () -> Unit = {
                                            val chatItemUI = ChatItemUI(
                                                pictureUrl = jobApplication.candidatePictureUrl
                                                    ?: "",
                                                profileName = jobApplication.candidateFullName
                                                    ?: "",
                                                offerTitle = jobApplication.jobOfferTitle ?: "",
                                                unreadMessageCount = 0,
                                                jobApplicationId = jobApplication.jobApplicationId
                                            )
                                            navigateToChatScreen(navController, chatItemUI)
                                        }

                                        enterpriseViewModel.chatExists(
                                            jobApplicationId = jobApplication.jobApplicationId,
                                            onSuccess = { chatIds ->
                                                if (chatIds.isNotEmpty()) {
                                                    onSuccess(chatIds)
                                                } else {
                                                    onFailure()
                                                }
                                            },
                                            onFailure = { onFailure() }
                                        )
                                    }
                                }
                            )

                            Spacer(modifier = Modifier.height(10.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ApplicationItemRow(
    context: Context,
    jobApplication: JobApplication,
    onNavigateToJobApplicationDetailsScreen: (JobApplication) -> Unit,
    onPreviewCandidateProfile: (String) -> Unit,
    onChatIconClicked: (JobApplication) -> Unit
) {
    Card(
        modifier = Modifier
            .wrapContentSize()
            .clickable {
                onNavigateToJobApplicationDetailsScreen(jobApplication)
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Column(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.background)
                .padding(15.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(context = LocalContext.current)
                        .data(jobApplication.candidatePictureUrl?.let {
                            URLEncoder.encode(
                                it,
                                "UTF-8"
                            )
                        })
                        .crossfade(true)
                        .build(),
                    contentDescription = stringResource(R.string.user_profile_picture_desc_text),
                    contentScale = ContentScale.Crop,
                    error = painterResource(id = R.drawable.user_profile_placeholder),
                    placeholder = painterResource(id = R.drawable.user_profile_placeholder),
                    modifier = Modifier
                        .size(50.dp)
                        .padding(4.dp)
                        .clip(CircleShape)
                        .clickable {
                            jobApplication.candidateProfileId?.let { onPreviewCandidateProfile(it) }
                        }
                )

                Spacer(modifier = Modifier.width(15.dp))

                Text(
                    text = jobApplication.candidateFullName ?: "",
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(10.dp))

                Icon(
                    painter = painterResource(id = R.drawable.ic_chat_menu_icon),
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
                            onChatIconClicked(jobApplication)
                        }
                )
            }

            if (!jobApplication.jobOfferTitle.isNullOrEmpty()) {
                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = stringResource(
                        R.string.applied_offer_info,
                        jobApplication.jobOfferTitle
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = stringResource(
                    R.string.application_status_info,
                    jobApplication.status ?: "-"
                ),
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 4,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )


            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = stringResource(
                    R.string.application_stages_info,
                    jobApplication.stages ?: "-"
                ),
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 4,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = Utils.getAppliedTimeAgo(
                    jobApplication.appliedAt ?: System.currentTimeMillis()
                ),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(5.dp))
        }
    }
}

fun navigateToChatScreen(navController: NavController, chatItemUI: ChatItemUI) {
    val chatItemUIJSON = Gson().toJson(chatItemUI)
    navController.navigate(route = "${ChatScreen.route}/$chatItemUIJSON")
}

private fun navigateToCandidateProfile(navController: NavController, candidateProfileId: String) {
    val isPreviewMode = true
    navController.navigate(route = "${CandidateProfileScreen.route}/$isPreviewMode/$candidateProfileId")
}

private fun navigateToApplicationDetails(
    navController: NavController,
    jobApplication: JobApplication
) {
    val jobApplicationJSON = Gson().toJson(jobApplication)
    navController.navigate(route = "${EditApplicationsDetailsScreen.route}/$jobApplicationJSON/${false}")
}