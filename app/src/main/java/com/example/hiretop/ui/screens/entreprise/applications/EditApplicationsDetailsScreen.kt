package com.example.hiretop.ui.screens.entreprise.applications

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
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
import com.example.hiretop.app.HireTop.Companion.appContext
import com.example.hiretop.models.JobApplication
import com.example.hiretop.navigation.NavDestination
import com.example.hiretop.ui.extras.DropdownListWords
import com.example.hiretop.ui.extras.FailurePopup
import com.example.hiretop.utils.Utils
import com.example.hiretop.viewModels.CandidateViewModel
import com.example.hiretop.viewModels.EnterpriseViewModel

object EditApplicationsDetailsScreen : NavDestination {
    override val route: String = "edit_applications_details_screen"
}

@Composable
fun EditApplicationsDetailsScreen(
    navController: NavController,
    jobApplication: JobApplication,
    isPreviewMode: Boolean,
    candidateViewModel: CandidateViewModel = hiltViewModel(),
    enterpriseViewModel: EnterpriseViewModel = hiltViewModel(),
) {
    val context = LocalContext.current

    var canWithdraw by remember { mutableStateOf(true) }
    var canReject by remember { mutableStateOf(true) }
    var onErrorMessage by remember { mutableStateOf<String?>(null) }

    if (!onErrorMessage.isNullOrEmpty()) {
        FailurePopup(errorMessage = "$onErrorMessage", onDismiss = {
            onErrorMessage = null
        })
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(15.dp)
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
            contentDescription = stringResource(R.string.back_arrow_icon_desc_text),
            modifier = Modifier.clickable {
                navController.navigateUp()
            }
        )

        Spacer(modifier = Modifier.height(30.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.background),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(jobApplication.candidatePictureUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = stringResource(id = R.string.user_profile_picture_desc_text),
                contentScale = ContentScale.Crop,
                error = painterResource(id = R.drawable.user_profile_placeholder),
                placeholder = painterResource(id = R.drawable.user_profile_placeholder),
                modifier = Modifier
                    .size(80.dp)
                    .padding(4.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = jobApplication.candidateFullName ?: "",
                style = MaterialTheme.typography.headlineSmall,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = context.getString(R.string.applied_offer_info, jobApplication.jobOfferTitle),
            style = MaterialTheme.typography.titleMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = Utils.getAppliedTimeAgo(context, jobApplication.appliedAt ?: System.currentTimeMillis()),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(height = 15.dp))

        if (isPreviewMode) {
            PreviewModeView(
                jobApplication = jobApplication,
                canWithdraw = canWithdraw,
                onWithdrawJobApplication = { jobApplication ->
                    candidateViewModel.withdrawJobApplication(
                        jobApplication = jobApplication,
                        onSuccess = {
                            canWithdraw = false
                        },
                        onFailure = { errorMessage ->
                            onErrorMessage = errorMessage
                        }
                    )
                })
        } else {
            EditModeView(
                jobApplication = jobApplication,
                canReject = canReject,
                onRejectApplicationClicked = { jobApplication ->
                    enterpriseViewModel.editJobApplication(
                        jobApplication = jobApplication,
                        onSuccess = {
                            canReject = false
                        },
                        onFailure = { message ->
                            onErrorMessage = message
                        }
                    )
                },
                onUpdateJobApplicationClicked = { jobApplication ->
                    enterpriseViewModel.editJobApplication(
                        jobApplication = jobApplication,
                        onSuccess = {},
                        onFailure = { message ->
                            onErrorMessage = message
                        }
                    )
                }
            )
        }
    }
}

@Composable
fun EditModeView(
    jobApplication: JobApplication,
    canReject: Boolean,
    onRejectApplicationClicked: (JobApplication) -> Unit,
    onUpdateJobApplicationClicked: (JobApplication) -> Unit
) {
    val mWidth = LocalConfiguration.current.screenWidthDp.dp
    val maxLength = 1500

    var selectedStatus by remember { mutableStateOf(jobApplication.status ?: "") }
    var requiredStages by remember { mutableStateOf(jobApplication.stages ?: "") }
    var makeAnOffer by remember { mutableStateOf(jobApplication.offerContent ?: "") }

    Column(
        modifier = Modifier.wrapContentSize()
    ) {
        DropdownListWords(
            title = stringResource(R.string.status_text),
            items = stringArrayResource(id = R.array.job_application_status),
            currentItemIndex = 0,
            onItemSelected = { selectedStatus = it }
        )

        Spacer(modifier = Modifier.height(height = 20.dp))

        OutlinedTextField(
            value = requiredStages,
            onValueChange = { requiredStages = it },
            label = {
                Text(
                    text = stringResource(R.string.required_stages_text),
                    style = MaterialTheme.typography.bodyLarge,
                )
            },
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(height = 25.dp))

        OutlinedTextField(
            value = makeAnOffer,
            onValueChange = { makeAnOffer = it },
            label = {
                Text(
                    text = stringResource(R.string.make_an_offer_text),
                    style = MaterialTheme.typography.bodyLarge,
                )
            },
            supportingText = {
                Text(
                    text = "${makeAnOffer.length} / $maxLength",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.End,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            },
            singleLine = false,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier
                .fillMaxWidth()
                .height(height = mWidth * 0.4f),
        )

        Spacer(modifier = Modifier.height(height = 25.dp))

        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            OutlinedButton(
                modifier = Modifier
                    .height(50.dp)
                    .weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.error
                ),
                enabled = canReject,
                border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.error),
                shape = MaterialTheme.shapes.large,
                onClick = {
                    onRejectApplicationClicked(
                        jobApplication.copy(
                            status = appContext.getString(
                                R.string.reject_text
                            )
                        )
                    )
                }
            ) {
                Text(
                    text = stringResource(R.string.reject_text),
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 18.sp)
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Button(
                modifier = Modifier
                    .height(50.dp)
                    .weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                shape = MaterialTheme.shapes.large,
                onClick = {
                    onUpdateJobApplicationClicked(
                        jobApplication.copy(
                            status = selectedStatus,
                            stages = requiredStages,
                            offerContent = makeAnOffer,
                            offerReceived = makeAnOffer.isEmpty()
                        )
                    )
                }
            ) {
                Text(
                    text = stringResource(R.string.modify_text),
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 18.sp)
                )
            }
        }
    }
}

@Composable
fun PreviewModeView(
    jobApplication: JobApplication,
    canWithdraw: Boolean,
    onWithdrawJobApplication: (JobApplication) -> Unit
) {
    Column(modifier = Modifier.wrapContentSize()) {
        Text(
            text = stringResource(
                R.string.application_status_info, jobApplication.status ?: "-"
            ),
            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp),
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
            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp),
            maxLines = 4,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = stringResource(
                R.string.offer_received_content_text,
                jobApplication.offerContent ?: ""
            ),
            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp),
            maxLines = 6,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(25.dp))

        OutlinedButton(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            enabled = canWithdraw,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.error
            ),
            border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.error),
            shape = MaterialTheme.shapes.large,
            onClick = { onWithdrawJobApplication(jobApplication.copy(withdraw = true)) }
        ) {
            Text(
                text = stringResource(R.string.withdraw_button_text),
                style = MaterialTheme.typography.titleLarge.copy(fontSize = 18.sp)
            )
        }

        Spacer(modifier = Modifier.height(5.dp))
    }
}