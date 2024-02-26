package com.example.hiretop.ui.screens.candidate.profile

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.hiretop.R
import com.example.hiretop.models.CandidateProfile
import com.example.hiretop.models.Certification
import com.example.hiretop.models.Education
import com.example.hiretop.models.Experience
import com.example.hiretop.models.Project
import com.example.hiretop.models.UIState
import com.example.hiretop.navigation.NavDestination
import com.example.hiretop.ui.extras.FailurePopup
import com.example.hiretop.ui.extras.HireTopBottomSheet
import com.example.hiretop.utils.Utils
import com.example.hiretop.utils.Utils.compressImage
import com.example.hiretop.viewModels.CandidateViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.util.UUID

object CandidateProfileScreen : NavDestination {
    override val route: String = "candidate_profile_screen"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CandidateProfileScreen(
    modifier: Modifier = Modifier,
    isPreviewMode: Boolean,
    argCandidateProfileId: String?,
    navController: NavController? = null,
    candidateViewModel: CandidateViewModel = hiltViewModel()
) {
    val candidateProfile by candidateViewModel.candidateProfile.collectAsState()
    val profileId by candidateViewModel.candidateProfileId.collectAsState(initial = argCandidateProfileId)

    val mContext = LocalContext.current
    var onErrorMessage by remember { mutableStateOf<String?>(null) }
    var sheetTitle by remember { mutableStateOf("") }
    var showBottomSheet by remember { mutableStateOf(false) }
    var bottomSheetContent by remember { mutableStateOf<@Composable (() -> Unit)?>(null) }

    // Define the update function
    val updateFunction: suspend (String?, CandidateProfile) -> Unit = { id, profile ->
        if (!id.isNullOrEmpty()) {
            candidateViewModel.updateCandidateProfile(
                profileId = id,
                editedProfile = profile,
                onSuccess = {},
                onFailure = { onErrorMessage = it }
            )
        } else {
            candidateViewModel.createNewProfile(
                candidateProfile = profile,
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

    LaunchedEffect(candidateViewModel) {
        if (isPreviewMode) {
            if (!profileId.isNullOrEmpty()) {
                candidateViewModel.getCandidateProfile(
                    profileId = "$argCandidateProfileId",
                    onSuccess = {},
                    onFailure = {
                        onErrorMessage = it
                    }
                )
            }
        } else {
            if (!profileId.isNullOrEmpty()) {
                candidateViewModel.getCandidateProfile(
                    profileId = "$profileId",
                    onSuccess = {},
                    onFailure = {
                        onErrorMessage = it
                    }
                )
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
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .background(color = MaterialTheme.colorScheme.background)
        ) {
            HeaderSection(
                profile = candidateProfile,
                isPreviewMode = isPreviewMode,
                onImageLoadingFailed = { error ->
                    onErrorMessage = error
                },
                onUploadBannerFile = { file ->
                    candidateViewModel.uploadFileToFirebaseStorageAndGetUrl(
                        inputStream = file.inputStream(),
                        fileName = file.nameWithoutExtension,
                        onSuccess = { downloadUrl ->
                            val profileCopy = candidateProfile?.copy(
                                bannerUrl = downloadUrl
                            ) ?: CandidateProfile(
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
                    candidateViewModel.uploadFileToFirebaseStorageAndGetUrl(
                        inputStream = file.inputStream(),
                        fileName = file.nameWithoutExtension,
                        onSuccess = { downloadUrl ->
                            val profileCopy = candidateProfile?.copy(
                                pictureUrl = downloadUrl
                            ) ?: CandidateProfile(
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
                onEditHeaderClicked = {
                    sheetTitle = mContext.getString(R.string.modify_summary_text)
                    bottomSheetContent = {
                        EditHeaderSection(
                            currentFirstname = candidateProfile?.firstname,
                            currentLastname = candidateProfile?.lastname,
                            currentHeadline = candidateProfile?.headline,
                            onSaveClicked = { firstname, lastname, headline ->
                                val profileCopy = candidateProfile?.copy(
                                    firstname = firstname,
                                    lastname = lastname,
                                    headline = headline
                                ) ?: CandidateProfile(
                                    firstname = firstname,
                                    lastname = lastname,
                                    headline = headline
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

            Spacer(modifier = Modifier.height(height = 15.dp))

            AboutSection(
                editedAbout = candidateProfile?.about,
                isPreviewMode = isPreviewMode,
                onEditAboutClicked = {
                    sheetTitle = mContext.getString(R.string.optional_about_text)
                    bottomSheetContent = {
                        EditProfileAboutSection(
                            currentValue = candidateProfile?.about,
                            onSaveClicked = { about ->
                                val profileCopy = candidateProfile?.copy(
                                    about = about,
                                ) ?: CandidateProfile(
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

            Spacer(modifier = Modifier.height(height = 15.dp))

            ExperienceSection(
                experiences = candidateProfile?.experiences,
                isPreviewMode = isPreviewMode,
                onAddOrEditExperienceClicked = { currentExperience ->
                    sheetTitle = mContext.getString(R.string.experience_text)
                    bottomSheetContent = {
                        EditOrAddExperienceSection(
                            currentValue = currentExperience,
                            onDeleteClicked = { itemToRemove ->
                                val profileCopy = candidateProfile?.copy(
                                    experiences = candidateProfile?.experiences?.filter { it != itemToRemove }
                                )

                                CoroutineScope(Dispatchers.IO).launch {
                                    if (profileCopy != null) {
                                        updateFunction(
                                            profileId,
                                            profileCopy
                                        )
                                    }
                                }

                                showBottomSheet = false
                            },
                            onSaveClicked = { experience ->
                                val editedExperiences = if (currentExperience == null) {
                                    candidateProfile?.experiences.orEmpty() + setOf(experience) // Add new experience
                                } else {
                                    candidateProfile?.experiences.orEmpty().toMutableSet().apply {
                                        this.add(experience) // Update current set of experiences
                                    }
                                }

                                val profileCopy = candidateProfile?.copy(
                                    experiences = editedExperiences.toList()
                                ) ?: CandidateProfile(
                                    experiences = editedExperiences.toList()
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

            Spacer(modifier = Modifier.height(height = 15.dp))

            EducationSection(
                educations = candidateProfile?.educations,
                isPreviewMode = isPreviewMode,
                onAddOrEditEducationClicked = { currentEducation ->
                    sheetTitle = mContext.getString(R.string.education_text)
                    bottomSheetContent = {
                        EditOrAddEducationSection(
                            currentValue = currentEducation,
                            onDeleteClicked = { itemToRemove ->
                                val profileCopy = candidateProfile?.copy(
                                    educations = candidateProfile?.educations?.filter { it != itemToRemove }
                                )

                                CoroutineScope(Dispatchers.IO).launch {
                                    if (profileCopy != null) {
                                        updateFunction(
                                            profileId,
                                            profileCopy
                                        )
                                    }
                                }

                                showBottomSheet = false
                            },
                            onSaveClicked = { education ->
                                val updatedEducations = if (currentEducation == null) {
                                    candidateProfile?.educations.orEmpty() + setOf(education) // Add new experience
                                } else {
                                    candidateProfile?.educations.orEmpty().toMutableSet().apply {
                                        this.add(education) // Update current set of experiences
                                    }
                                }

                                val profileCopy = candidateProfile?.copy(
                                    educations = updatedEducations.toList()
                                ) ?: CandidateProfile(
                                    educations = updatedEducations.toList()
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

            Spacer(modifier = Modifier.height(height = 15.dp))

            CertificationsSection(
                certifications = candidateProfile?.certifications,
                isPreviewMode = isPreviewMode,
                onAddOrEditCertificationClicked = { currentCertification ->
                    sheetTitle = mContext.getString(R.string.certifications_text)
                    bottomSheetContent = {
                        EditOrAddCertificationSection(
                            currentValue = currentCertification,
                            onDeleteClicked = { itemToRemove ->
                                val profileCopy = candidateProfile?.copy(
                                    certifications = candidateProfile?.certifications?.filter { it != itemToRemove }
                                )

                                CoroutineScope(Dispatchers.IO).launch {
                                    if (profileCopy != null) {
                                        updateFunction(
                                            profileId,
                                            profileCopy
                                        )
                                    }
                                }

                                showBottomSheet = false
                            },
                            onSaveClicked = { certification ->
                                val updatedCertifications = if (currentCertification == null) {
                                    candidateProfile?.certifications.orEmpty() + setOf(certification) // Add new experience
                                } else {
                                    candidateProfile?.certifications.orEmpty().toMutableSet()
                                        .apply {
                                            this.add(certification) // Update current set of experiences
                                        }
                                }

                                val profileCopy = candidateProfile?.copy(
                                    certifications = updatedCertifications.toList()
                                ) ?: CandidateProfile(
                                    certifications = updatedCertifications.toList()
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

            Spacer(modifier = Modifier.height(height = 15.dp))

            ProjectsSection(
                projects = candidateProfile?.projects,
                isPreviewMode = isPreviewMode,
                onAddOrEditProjectClicked = { currentProject ->
                    sheetTitle = mContext.getString(R.string.projects_text)
                    bottomSheetContent = {
                        EditOrAddProjectSection(
                            currentValue = currentProject,
                            onDeleteClicked = { itemToRemove ->
                                val profileCopy = candidateProfile?.copy(
                                    projects = candidateProfile?.projects?.filter { it != itemToRemove }
                                )

                                CoroutineScope(Dispatchers.IO).launch {
                                    if (profileCopy != null) {
                                        updateFunction(
                                            profileId,
                                            profileCopy
                                        )
                                    }
                                }

                                showBottomSheet = false
                            },
                            onSaveClicked = { project ->
                                val updatedProjects = if (currentProject == null) {
                                    candidateProfile?.projects.orEmpty() + setOf(project) // Add new experience
                                } else {
                                    candidateProfile?.projects.orEmpty().toMutableSet().apply {
                                        this.add(project) // Update current set of experiences
                                    }
                                }

                                val profileCopy = candidateProfile?.copy(
                                    projects = updatedProjects.toList()
                                ) ?: CandidateProfile(
                                    projects = updatedProjects.toList()
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

            Spacer(modifier = Modifier.height(height = 15.dp))

            SkillsSection(
                skills = candidateProfile?.skills?.joinToString(separator = ","),
                isPreviewMode = isPreviewMode,
                onAddOrEditSkillsClicked = { currentSkills ->
                    sheetTitle = mContext.getString(R.string.optional_skills_text)
                    bottomSheetContent = {
                        EditOrAddSkillSection(
                            currentValue = currentSkills,
                            onSaveClicked = { skills ->
                                val profileCopy = candidateProfile?.copy(
                                    skills = skills.toList(),
                                ) ?: CandidateProfile(
                                    skills = skills.toList()
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

            Spacer(modifier = Modifier.height(height = 15.dp))
        }
    }
}

@Composable
private fun HeaderSection(
    profile: CandidateProfile?,
    isPreviewMode: Boolean,
    onImageLoadingFailed: (String) -> Unit,
    onEditHeaderClicked: () -> Unit,
    onUploadBannerFile: (File) -> Unit,
    onUploadProfilePictureFile: (File) -> Unit
) {
    val context = LocalContext.current
    var requestCode = 0
    val mWidth = LocalConfiguration.current.screenWidthDp.dp
    val bannerFilePath by remember { mutableStateOf(profile?.bannerUrl) }
    val profilePictureFilePath by remember { mutableStateOf(profile?.pictureUrl) }
    // Activity result launcher for picking an image from gallery
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { imageUri ->
        if (imageUri != null) {
            try {
                // Load and set the compressed image to the appropriate AsyncImage
                if (requestCode == 1) {
                    val bannerFile = compressImage(
                        context = context, uri = imageUri,
                        filename = Utils.extractStringFromLink("${profile?.bannerUrl}")
                            ?: "${UUID.randomUUID()}_compressed_banner.png",
                        callback = {
                            onImageLoadingFailed(it)
                        }
                    )
                    bannerFile?.let { onUploadBannerFile(it) }
                } else if (requestCode == 2) {
                    val profilePictureFile = compressImage(
                        context = context, uri = imageUri,
                        filename = Utils.extractStringFromLink("${profile?.pictureUrl}")
                            ?: "${UUID.randomUUID()}_compressed_profile_picture.png",
                        callback = {
                            onImageLoadingFailed(it)
                        }
                    )
                    profilePictureFile?.let { onUploadProfilePictureFile(it) }
                }
            } catch (e: Exception) {
                onImageLoadingFailed(e.message ?: context.getString(R.string.unkown_error_text))
            }
        } else {
//            onImageLoadingFailed(context.getString(R.string.no_image_picked_error_text))
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
                onEditHeaderClicked()
            }
        }) {
            Text(
                text = if (profile != null && !profile.firstname.isNullOrEmpty() && !profile.lastname.isNullOrEmpty())
                    profile.name
                else
                    stringResource(R.string.firstname_lastname_info),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(horizontal = 15.dp)
            )

            Text(
                text = profile?.headline ?: stringResource(R.string.profile_headline_info),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 5,
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
    isPreviewMode: Boolean, onEditAboutClicked: () -> Unit
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
                    modifier = Modifier.clickable { onEditAboutClicked() }
                )
            }
        }

        Text(
            text = editedAbout ?: stringResource(R.string.profil_presentation_info),
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

@Composable
private fun ExperienceSection(
    experiences: List<Experience>?,
    isPreviewMode: Boolean,
    onAddOrEditExperienceClicked: (Experience?) -> Unit
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
                text = stringResource(R.string.experience_text),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.weight(weight = 1f))

            if (!isPreviewMode) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.add_experience_icon_desc_text),
                    modifier = Modifier
                        .size(size = 32.dp)
                        .clickable { onAddOrEditExperienceClicked(null) }
                )
            }
        }

        if (experiences.isNullOrEmpty()) {
            Text(
                text = stringResource(R.string.no_experience_text),
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Normal),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp)
            )
        } else {
            val sortedExperiences =
                experiences.toList().sortedByDescending { it.getStartDate() }.toSet()
            sortedExperiences.forEach { item ->
                ExperienceItemRow(
                    experience = item,
                    onEditExperience = {
                        if (!isPreviewMode) {
                            onAddOrEditExperienceClicked(it)
                        }
                    })
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
private fun ExperienceItemRow(experience: Experience, onEditExperience: (Experience) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onEditExperience(experience) }
            .padding(horizontal = 15.dp)
    ) {
        Text(
            text = experience.companyName ?: "•",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(15.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(8.dp)
                        .background(color = MaterialTheme.colorScheme.primary)
                )

                Spacer(modifier = Modifier.width(15.dp))

                Text(
                    text = experience.title ?: "•",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Column(modifier = Modifier.padding(start = 25.dp)) {
                Text(
                    text = experience.employmentType ?: "•",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Normal),
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(height = 3.dp))
                Text(
                    text = "${
                        experience.startMonth?.lowercase()
                    } ${experience.startYear} - ${
                        experience.endMonth?.lowercase()
                    } ${experience.endYear}",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Normal),
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(height = 3.dp))
                Text(
                    text = "${experience.location} • ${experience.locationType}",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Normal),
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(height = 3.dp))
                Text(
                    text = experience.description ?: "•",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Normal),
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 20,
                    overflow = TextOverflow.Ellipsis
                )

                if (!experience.skills.isNullOrEmpty()) {
                    Spacer(modifier = Modifier.height(height = 15.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_skills_icon),
                            contentDescription = stringResource(R.string.skills_icon_desc_text),
                            modifier = Modifier.size(size = 14.dp)
                        )

                        Spacer(modifier = Modifier.width(5.dp))

                        Text(
                            text = experience.skills.joinToString(separator = "|"),
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EducationSection(
    educations: List<Education>?,
    isPreviewMode: Boolean,
    onAddOrEditEducationClicked: (Education?) -> Unit
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
                text = stringResource(R.string.education_text),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.weight(weight = 1f))

            if (!isPreviewMode) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.add_education_icon_desc_text),
                    modifier = Modifier
                        .size(size = 32.dp)
                        .clickable { onAddOrEditEducationClicked(null) }
                )
            }
        }

        if (educations.isNullOrEmpty()) {
            Text(
                text = stringResource(R.string.no_education_text),
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Normal),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp)
            )
        } else {
            val sortedEducations =
                educations.toList().sortedByDescending { it.getStartDate() }.toSet()
            sortedEducations.forEach { item ->
                EducationItemRow(
                    education = item,
                    onEditEducation = {
                        if (!isPreviewMode) {
                            onAddOrEditEducationClicked(it)
                        }
                    }
                )
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
private fun EducationItemRow(education: Education, onEditEducation: (Education) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onEditEducation(education) }
            .padding(horizontal = 15.dp)
    ) {
        Text(
            text = education.school ?: "•",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            text = "${education.degree} • ${education.fieldOfStudy}",
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Normal),
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(height = 3.dp))

        Text(
            text = "${education.startYear} - ${education.endYear}",
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Normal),
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun CertificationsSection(
    certifications: List<Certification>?,
    isPreviewMode: Boolean,
    onAddOrEditCertificationClicked: (Certification?) -> Unit
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
                text = stringResource(R.string.certifications_text),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.weight(weight = 1f))

            if (!isPreviewMode) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.add_certification_icon_desc_text),
                    modifier = Modifier
                        .size(size = 32.dp)
                        .clickable {
                            onAddOrEditCertificationClicked(null) // Add new certification
                        }
                )
            }
        }

        if (certifications.isNullOrEmpty()) {
            Text(
                text = stringResource(R.string.no_certification_text),
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Normal),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp)
            )
        } else {
            val sortedCertifications =
                certifications.toList().sortedByDescending { it.getStartDate() }.toSet()
            sortedCertifications.forEach { item ->
                CertificationItemRow(certification = item,
                    onEditCertification = {
                        if (!isPreviewMode) {
                            onAddOrEditCertificationClicked(it)
                        }
                    })
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
private fun CertificationItemRow(
    certification: Certification,
    onEditCertification: (Certification) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onEditCertification(certification) }
            .padding(horizontal = 15.dp)
    ) {
        Text(
            text = certification.name ?: "•",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            text = certification.issuingOrganization ?: "•",
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Normal),
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(height = 3.dp))

        var certificationDate = "${stringResource(id = R.string.optional_issued_date_text)} : ${
            certification.issueMonth?.lowercase()?.substring(0, 3)
        } ${certification.issueYear}"

        // Add expiration date if defined
        if (!certification.expireMonth.isNullOrEmpty() && !certification.expireYear.isNullOrEmpty()) {
            certificationDate = stringResource(
                R.string.certification_issued_and_expired_date_text,
                certification.issueMonth?.lowercase().toString(),
                certification.issueYear.toString(),
                certification.expireMonth,
                certification.expireYear
            )
        }

        Text(
            text = certificationDate,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Normal),
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(height = 3.dp))

        if (!certification.credentialID.isNullOrEmpty()) {
            Text(
                text = stringResource(
                    R.string.certification_id_text,
                    "${certification.credentialID}"
                ),
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Normal),
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun ProjectsSection(
    projects: List<Project>?,
    isPreviewMode: Boolean,
    onAddOrEditProjectClicked: (Project?) -> Unit
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
                text = stringResource(R.string.projects_text),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.weight(weight = 1f))

            if (!isPreviewMode) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.add_project_icon_desc_text),
                    modifier = Modifier
                        .size(size = 32.dp)
                        .clickable { onAddOrEditProjectClicked(null) }
                )
            }
        }

        if (projects.isNullOrEmpty()) {
            Text(
                text = stringResource(R.string.no_project_text),
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Normal),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp)
            )
        } else {
            projects.forEach { item ->
                ProjectItemRow(project = item,
                    onEditProject = {
                        if (!isPreviewMode) {
                            onAddOrEditProjectClicked(it)
                        }
                    })
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
private fun ProjectItemRow(project: Project, onEditProject: (Project) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onEditProject(project) }
            .padding(horizontal = 15.dp)
    ) {
        Text(
            text = project.name ?: "•",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(height = 5.dp))

        Text(
            text = project.description ?: "•",
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Normal),
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 5,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(height = 10.dp))

        if (!project.skills.isNullOrEmpty()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_skills_icon),
                    contentDescription = stringResource(id = R.string.skills_icon_desc_text),
                    modifier = Modifier.size(size = 14.dp)
                )

                Spacer(modifier = Modifier.width(5.dp))

                Text(
                    text = project.skills.joinToString(separator = ","),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}

@Composable
private fun SkillsSection(
    skills: String?,
    isPreviewMode: Boolean, onAddOrEditSkillsClicked: (String?) -> Unit
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
                text = stringResource(id = R.string.optional_skills_text),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.weight(weight = 1f))

            if (!isPreviewMode) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.add_skills_icon_desc_text),
                    modifier = Modifier
                        .size(size = 32.dp)
                        .clickable { onAddOrEditSkillsClicked(skills) }
                )
            }
        }

        Text(
            text = if (!skills.isNullOrEmpty()) skills else stringResource(R.string.no_skills_added_text),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .clickable {
                    if (!isPreviewMode) {
                        onAddOrEditSkillsClicked(skills)
                    }
                }
                .padding(horizontal = 15.dp)
        )

    }
}