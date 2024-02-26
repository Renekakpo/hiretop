package com.example.hiretop.ui.screens.entreprise.presentation

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.outlined.Edit
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.hiretop.R
import com.example.hiretop.models.CandidateProfile
import com.example.hiretop.models.EnterpriseProfile
import com.example.hiretop.navigation.NavDestination
import com.example.hiretop.ui.extras.FailurePopup
import com.example.hiretop.ui.extras.HireTopBottomSheet
import com.example.hiretop.utils.Utils
import com.example.hiretop.viewModels.CandidateViewModel
import com.example.hiretop.viewModels.EnterpriseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.util.UUID

object EnterpriseProfileScreen : NavDestination {
    override val route: String = "enterprise_profile_screen"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnterpriseProfileScreen(
    modifier: Modifier = Modifier,
    isPreviewMode: Boolean,
    argEnterpriseProfileId: String?,
    navController: NavController? = null,
    enterpriseViewModel: EnterpriseViewModel = hiltViewModel()
) {
    val enterpriseProfile by enterpriseViewModel.enterpriseProfile.collectAsState()
    val profileId by enterpriseViewModel.enterpriseProfileId.collectAsState(initial = argEnterpriseProfileId)

    val mContext = LocalContext.current
    var sheetTitle by remember { mutableStateOf("") }
    var showBottomSheet by remember { mutableStateOf(false) }
    var bottomSheetContent by remember { mutableStateOf<@Composable (() -> Unit)?>(null) }
    var onErrorMessage by remember { mutableStateOf<String?>(null) }

    // Define the update function
    val updateFunction: suspend (String?, EnterpriseProfile) -> Unit = { id, profile ->
        if (!id.isNullOrEmpty()) {
            enterpriseViewModel.updateEnterpriseProfile(
                enterpriseId = id,
                updatedProfile = profile,
                onSuccess = {},
                onFailure = { onErrorMessage = it }
            )
        } else {
            enterpriseViewModel.createNewEnterpriseProfile(
                profile = profile,
                onSuccess = {},
                onFailure = { onErrorMessage = it }
            )
        }
    }

    if (!onErrorMessage.isNullOrEmpty()) {
        FailurePopup(errorMessage = "$onErrorMessage", onDismiss = {
            onErrorMessage = null // Reset error message
        })
    }

    if (showBottomSheet && bottomSheetContent != null) {
        HireTopBottomSheet(
            title = sheetTitle,
            onDismiss = { showBottomSheet = false }) {
            bottomSheetContent?.invoke()
        }
    }

    LaunchedEffect(enterpriseViewModel) {
        if (isPreviewMode) {
            if (!argEnterpriseProfileId.isNullOrEmpty()) {
                enterpriseViewModel.getEnterpriseProfile(
                    enterpriseId = "$argEnterpriseProfileId",
                    onSuccess = {},
                    onFailure = {})
            }
        } else {
            if (!profileId.isNullOrEmpty()) {
                enterpriseViewModel.getEnterpriseProfile(
                    enterpriseId = "$profileId",
                    onSuccess = {},
                    onFailure = {})
            }
        }
    }

    Scaffold(
        topBar = {
            if (isPreviewMode) {
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
                            modifier = Modifier.clickable { navController?.navigateUp() }
                        )
                    },
                    title = {
                        Text(
                            text = stringResource(R.string.candidate_profile_preview),
                            style = MaterialTheme.typography.headlineSmall,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.wrapContentWidth()
                        )
                    }
                )
            }
        },
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .background(color = MaterialTheme.colorScheme.background)
        ) {
            HeaderSection(
                profile = enterpriseProfile,
                isPreviewMode = isPreviewMode,
                onImageLoadingFailed = { error ->
                    onErrorMessage = error
                },
                onUploadBannerFile = { file ->
                    enterpriseViewModel.uploadFileToFirebaseStorageAndGetUrl(
                        inputStream = file.inputStream(),
                        fileName = file.nameWithoutExtension,
                        onSuccess = { downloadUrl ->
                            val profileCopy = enterpriseProfile?.copy(
                                bannerUrl = downloadUrl
                            ) ?: EnterpriseProfile(
                                bannerUrl = downloadUrl
                            )

                            CoroutineScope(Dispatchers.IO).launch {
                                updateFunction(
                                    profileId,
                                    profileCopy
                                )
                            }
                        },
                        onFailure = {
                            onErrorMessage = it
                        }
                    )
                },
                onUploadProfilePictureFile = { file ->
                    enterpriseViewModel.uploadFileToFirebaseStorageAndGetUrl(
                        inputStream = file.inputStream(),
                        fileName = file.nameWithoutExtension,
                        onSuccess = { downloadUrl ->
                            val profileCopy = enterpriseProfile?.copy(
                                pictureUrl = downloadUrl
                            ) ?: EnterpriseProfile(
                                pictureUrl = downloadUrl
                            )

                            CoroutineScope(Dispatchers.IO).launch {
                                updateFunction(
                                    profileId,
                                    profileCopy
                                )
                            }
                        },
                        onFailure = {
                            onErrorMessage = it
                        }
                    )
                },
                onEditHeaderClicked = { updatedName, updatedIndustry, updatedHeadline, updateLocation ->
                    sheetTitle = mContext.getString(R.string.modify_summary_text)
                    bottomSheetContent = {
                        EditProfileHeaderSection(
                            currentName = updatedName,
                            currentIndustry = updatedIndustry,
                            currentHeadline = updatedHeadline,
                            currentLocation = updateLocation,
                            onSaveClicked = { name, industry, headline, location ->
                                val profileCopy = enterpriseProfile?.copy(
                                    name = name,
                                    industry = industry,
                                    headline = headline,
                                    location = location
                                ) ?: EnterpriseProfile(
                                    name = name,
                                    industry = industry,
                                    headline = headline,
                                    location = location
                                )

                                CoroutineScope(Dispatchers.IO).launch {
                                    updateFunction(
                                        profileId,
                                        profileCopy
                                    )
                                }

                                showBottomSheet = false
                            })
                    }
                    showBottomSheet = true
                })

            Spacer(modifier = Modifier.height(height = 20.dp))

            AboutSection(
                editedAbout = enterpriseProfile?.about,
                isPreviewMode = isPreviewMode,
                onEditAboutClicked = { editedAbout ->
                    sheetTitle = mContext.getString(R.string.optional_about_text)
                    bottomSheetContent = {
                        EditEnterpriseAboutSection(
                            currentAbout = editedAbout,
                            onSaveClicked = { about ->
                                val profileCopy = enterpriseProfile?.copy(
                                    about = about,
                                ) ?: EnterpriseProfile(
                                    about = about
                                )

                                CoroutineScope(Dispatchers.IO).launch {
                                    updateFunction(
                                        profileId,
                                        profileCopy
                                    )
                                }

                                showBottomSheet = false
                            })
                    }
                    showBottomSheet = true
                })

            Spacer(modifier = Modifier.height(height = 20.dp))

            CultureAndValuesSection(
                updatedCultureAndValues = enterpriseProfile?.about,
                isPreviewMode = isPreviewMode,
                onEditCultureAndValuesClicked = { updatedCultureAndValues ->
                    sheetTitle = mContext.getString(R.string.enterprise_culture_values_text)
                    bottomSheetContent = {
                        EditEnterpriseCultureAndValuesSection(
                            currentCultureAndValues = updatedCultureAndValues,
                            onSaveClicked = { cultureAndValues ->
                                val profileCopy = enterpriseProfile?.copy(
                                    cultureAndValues = cultureAndValues,
                                ) ?: EnterpriseProfile(
                                    cultureAndValues = cultureAndValues
                                )

                                CoroutineScope(Dispatchers.IO).launch {
                                    updateFunction(
                                        profileId,
                                        profileCopy
                                    )
                                }

                                showBottomSheet = false
                            })
                    }
                    showBottomSheet = true
                })

            Spacer(modifier = Modifier.height(height = 20.dp))

            EnterpriseContactDetailsSection(
                updatedContactDetails = enterpriseProfile?.contactDetails,
                isPreviewMode = isPreviewMode,
                onEditContactDetailsClicked = {
                    sheetTitle = mContext.getString(R.string.enterprise_contact_details_text)
                    bottomSheetContent = {
                        EditEnterpriseContactDetailsSection(
                            currentContactDetails = enterpriseProfile?.contactDetails,
                            onSaveClicked = { contactDetails ->
                                val profileCopy = enterpriseProfile?.copy(
                                    contactDetails = contactDetails,
                                ) ?: EnterpriseProfile(
                                    contactDetails = contactDetails
                                )

                                CoroutineScope(Dispatchers.IO).launch {
                                    updateFunction(
                                        profileId,
                                        profileCopy
                                    )
                                }
                                showBottomSheet = false
                            })
                    }
                    showBottomSheet = true
                })
        }
    }
}

@Composable
private fun HeaderSection(
    profile: EnterpriseProfile?,
    isPreviewMode: Boolean,
    onImageLoadingFailed: (String) -> Unit,
    onEditHeaderClicked: (String, String, String, String) -> Unit,
    onUploadBannerFile: (File) -> Unit,
    onUploadProfilePictureFile: (File) -> Unit
) {
    val context = LocalContext.current
    var requestCode = 0
    val mWidth = LocalConfiguration.current.screenWidthDp.dp
    val bannerFilePath by remember { mutableStateOf(profile?.bannerUrl ?: "") }
    val profilePictureFilePath by remember { mutableStateOf(profile?.pictureUrl ?: "") }
    // Activity result launcher for picking an image from gallery
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { imageUri ->
        if (imageUri != null) {
            try {
                // Load and set the compressed image to the appropriate AsyncImage
                if (requestCode == 1) {
                    val bannerFile = Utils.compressImage(
                        context, imageUri,
                        "${UUID.randomUUID()}_compressed_banner.png"
                    )
                    bannerFile?.let { onUploadBannerFile(it) }
                } else if (requestCode == 2) {
                    val profilePictureFile = Utils.compressImage(
                        context, imageUri,
                        "${UUID.randomUUID()}_compressed_profile_picture.png"
                    )
                    profilePictureFile?.let { onUploadProfilePictureFile(it) }
                }
            } catch (e: Exception) {
                onImageLoadingFailed(e.message ?: context.getString(R.string.unkown_error_text))
            }
        } else {
            onImageLoadingFailed(context.getString(R.string.no_image_picked_error_text))
        }
    }

    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = mWidth / 2)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(bannerFilePath)
                    .crossfade(true)
                    .build(),
                contentDescription = stringResource(R.string.profile_banner_desc_text),
                contentScale = ContentScale.FillBounds,
                error = painterResource(id = R.drawable.banner_placeholder),
                placeholder = painterResource(id = R.drawable.banner_placeholder),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height = mWidth / 3.5F)
                    .align(alignment = Alignment.TopStart)
                    .clickable {
                        if (!isPreviewMode) {
                            requestCode = 1
                            galleryLauncher.launch("image/*")
                        }
                    }
            )

            // Edit banner icon
            if (!isPreviewMode) {
                Box(
                    modifier = Modifier
                        .padding(7.dp)
                        .align(alignment = Alignment.TopEnd)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Edit,
                        contentDescription = stringResource(R.string.edit_icon_desc_text),
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier
                            .clickable {
                                requestCode = 1
                                galleryLauncher.launch("image/*")
                            }
                            .background(
                                color = MaterialTheme.colorScheme.background,
                                shape = CircleShape
                            )
                            .align(alignment = Alignment.Center)
                            .padding(3.dp)
                    )
                }
            }

            // Profile picture
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(profilePictureFilePath)
                    .crossfade(true)
                    .build(),
                contentDescription = stringResource(R.string.user_profile_picture_desc_text),
                contentScale = ContentScale.Crop,
                error = painterResource(id = R.drawable.user_profile_placeholder),
                placeholder = painterResource(id = R.drawable.user_profile_placeholder),
                modifier = Modifier
                    .size(150.dp)
                    .padding(4.dp)
                    .clip(CircleShape)
                    .align(alignment = Alignment.BottomStart)
                    .clickable {
                        if (!isPreviewMode) {
                            requestCode = 2
                            galleryLauncher.launch("image/*")
                        }
                    }
            )

            // Add Icon
            if (!isPreviewMode) {
                Box(
                    modifier = Modifier
                        .padding(15.dp)
                        .size(size = 130.dp)
                        .align(alignment = Alignment.BottomStart)
                        .clickable {
                            requestCode = 2
                            galleryLauncher.launch("image/*")
                        }
                ) {
                    Icon(
                        imageVector = Icons.Filled.AddCircle,
                        contentDescription = stringResource(R.string.edit_profile_icon_text),
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .size(size = 32.dp)
                            .align(alignment = Alignment.BottomEnd)
                            .background(
                                color = MaterialTheme.colorScheme.onPrimary,
                                shape = CircleShape
                            )
                    )
                }
            }
        }

        Column(modifier = Modifier.clickable {
            if (!isPreviewMode) {
                onEditHeaderClicked(
                    "${profile?.name}",
                    "${profile?.industry}",
                    "${profile?.headline}",
                    "${profile?.location}",
                )
            }
        }) {
            Text(
                text = if (profile != null && !profile.name.isNullOrEmpty())
                    profile.name
                else
                    stringResource(R.string.enterprise_name_info),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(horizontal = 15.dp)
            )

            Text(
                text = profile?.headline ?: stringResource(R.string.enterprise_headling_info),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(horizontal = 15.dp)
            )

            Text(
                text = profile?.industry ?: stringResource(R.string.industry_and_location_info),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.outline,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(horizontal = 15.dp)
            )
        }
    }
}

@Composable
private fun AboutSection(
    editedAbout: String?,
    isPreviewMode: Boolean,
    onEditAboutClicked: (String?) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
    ) {
        Divider(modifier = Modifier.padding(horizontal = 15.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.optional_about_text),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.weight(weight = 1f))

            if (!isPreviewMode) {
                Icon(
                    imageVector = Icons.Outlined.Edit,
                    contentDescription = stringResource(R.string.edit_icon_desc_text),
                    modifier = Modifier.clickable { onEditAboutClicked(editedAbout) }
                )
            }
        }

        Text(
            text = editedAbout ?: stringResource(R.string.enterprise_presentation_info),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 50,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .padding(horizontal = 15.dp)
                .fillMaxWidth()
        )
    }
}

@Composable
private fun CultureAndValuesSection(
    updatedCultureAndValues: String?,
    isPreviewMode: Boolean,
    onEditCultureAndValuesClicked: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
    ) {
        Divider(modifier = Modifier.padding(horizontal = 15.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.enterprise_culture_values_text),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.weight(weight = 1f))

            if (!isPreviewMode) {
                Icon(
                    imageVector = Icons.Outlined.Edit,
                    contentDescription = stringResource(R.string.edit_icon_desc_text),
                    modifier = Modifier.clickable { onEditCultureAndValuesClicked("$updatedCultureAndValues") }
                )
            }
        }

        Text(
            text = updatedCultureAndValues
                ?: stringResource(R.string.enterprise_culture_values_info),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 30,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .padding(horizontal = 15.dp)
                .fillMaxWidth()
        )
    }
}

@Composable
private fun EnterpriseContactDetailsSection(
    updatedContactDetails: String?,
    isPreviewMode: Boolean,
    onEditContactDetailsClicked: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
    ) {
        Divider(modifier = Modifier.padding(horizontal = 15.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.enterprise_contact_details_text),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.weight(weight = 1f))

            if (!isPreviewMode) {
                Icon(
                    imageVector = Icons.Outlined.Edit,
                    contentDescription = stringResource(R.string.edit_icon_desc_text),
                    modifier = Modifier.clickable { onEditContactDetailsClicked("$updatedContactDetails") }
                )
            }
        }

        Text(
            text = updatedContactDetails
                ?: stringResource(R.string.enterprise_contact_details_info),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 10,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .padding(horizontal = 15.dp)
                .fillMaxWidth()
        )
    }
}