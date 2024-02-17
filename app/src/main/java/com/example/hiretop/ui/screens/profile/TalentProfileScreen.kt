package com.example.hiretop.ui.screens.profile

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.getString
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.hiretop.R
import com.example.hiretop.ui.extras.HireTopBottomSheet

@Composable
fun TalentProfileScreen() {
    val mContext = LocalContext.current
    val mWidth = LocalConfiguration.current.screenWidthDp.dp
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        HeaderSection(width = mWidth)

        Spacer(modifier = Modifier.height(height = 15.dp))

        AboutSection(onEditAboutClicked = {
            sheetTitle = mContext.getString(R.string.optional_about_text)
            bottomSheetContent = {
                EditProfileAboutSection()
            }
            showBottomSheet = true
        })

        Spacer(modifier = Modifier.height(height = 15.dp))

        ExperienceSection(onEditExperienceClicked = {
            sheetTitle = mContext.getString(R.string.experience_text)
            bottomSheetContent = {
                EditOrAddExperienceSection()
            }
            showBottomSheet = true
        })

        Spacer(modifier = Modifier.height(height = 15.dp))

        EducationSection(onEditEducationClicked = {
            sheetTitle = mContext.getString(R.string.education_text)
            bottomSheetContent = {
                EditOrAddEducationSection()
            }
            showBottomSheet = true
        })

        Spacer(modifier = Modifier.height(height = 15.dp))

        CertificationsSection(onEditCertificationClicked = {
            sheetTitle = mContext.getString(R.string.certifications_text)
            bottomSheetContent = {
                EditOrAddCertificationSection()
            }
            showBottomSheet = true
        })

        Spacer(modifier = Modifier.height(height = 15.dp))

        ProjectsSection(onEditProjectClicked = {
            sheetTitle = mContext.getString(R.string.projects_text)
            bottomSheetContent = {
                EditOrAddProjectSection()
            }
            showBottomSheet = true
        })

        Spacer(modifier = Modifier.height(height = 15.dp))

        SkillsSection(onEditSkillsClicked = {
            sheetTitle = mContext.getString(R.string.optional_skills_text)
            bottomSheetContent = {
                EditOrAddSkillSection()
            }
            showBottomSheet = true
        })

        Spacer(modifier = Modifier.height(height = 15.dp))
    }
}

@Composable
fun HeaderSection(width: Dp) {
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = width / 2)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data("")
                    .crossfade(true)
                    .build(),
                contentDescription = "user profile picture",
                contentScale = ContentScale.FillBounds,
                error = painterResource(id = R.drawable.user_profile_banner),
                placeholder = painterResource(id = R.drawable.user_profile_banner),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height = width / 3.5F)
                    .align(alignment = Alignment.TopStart)
            )

            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data("")
                    .crossfade(true)
                    .build(),
                contentDescription = "user profile picture",
                contentScale = ContentScale.Crop,
                error = painterResource(id = R.drawable.ai_profile_picture),
                placeholder = painterResource(id = R.drawable.ai_profile_picture),
                modifier = Modifier
                    .size(150.dp)
                    .padding(4.dp)
                    .clip(CircleShape)
                    .align(alignment = Alignment.BottomStart)
            )


            Box(
                modifier = Modifier
                    .padding(15.dp)
                    .size(size = 130.dp)
                    .align(alignment = Alignment.BottomStart)
            ) {
                Icon(
                    imageVector = Icons.Filled.AddCircle,
                    contentDescription = "Modify profile picture",
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

        Text(
            text = "Lorem Ipsum",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(horizontal = 15.dp)
        )

        Text(
            text = "Profile headline on 5 lines max",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 5,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(horizontal = 15.dp)
        )
    }
}

@Composable
fun AboutSection(onEditAboutClicked: () -> Unit) {
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
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.weight(weight = 1f))

            Icon(
                painter = painterResource(id = R.drawable.ic_mode_edit_outline_24),
                contentDescription = stringResource(R.string.edit_icon_desc_text),
                modifier = Modifier.clickable { onEditAboutClicked() }
            )
        }

        Text(
            text = "Profile description goes here..",
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
fun ExperienceSection(onEditExperienceClicked: () -> Unit) {
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
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.weight(weight = 1f))

            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = stringResource(R.string.add_experience_icon_desc_text),
                modifier = Modifier
                    .size(size = 32.dp)
                    .clickable { onEditExperienceClicked() }
            )
        }

        ExperienceItemRow()
    }
}

@Composable
fun ExperienceItemRow() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp)
    ) {
        Text(
            text = "Company Name",
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
                    text = "Job position Développeur Mobile",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Column(modifier = Modifier.padding(start = 25.dp)) {
                Text(
                    text = "Job type: Contract or Freelance",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Normal),
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(height = 3.dp))
                Text(
                    text = "Start date - End date: Jan 20223 - Dec 2024",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Normal),
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(height = 3.dp))
                Text(
                    text = "Job location: Benin - Location type: Hybrid",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Normal),
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(height = 3.dp))
                Text(
                    text = "Job description or accomplishment",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Normal),
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 20,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(height = 15.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_skills_icon),
                        contentDescription = "Skills icon",
                        modifier = Modifier.size(size = 14.dp)
                    )

                    Spacer(modifier = Modifier.width(5.dp))

                    Text(
                        text = "Skills",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
    }
}

@Composable
fun EducationSection(onEditEducationClicked: () -> Unit) {
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
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.weight(weight = 1f))

            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = stringResource(R.string.add_education_icon_desc_text),
                modifier = Modifier.size(size = 32.dp).clickable { onEditEducationClicked() }
            )
        }

        EducationItemRow()
    }
}

@Composable
fun EducationItemRow() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp)
    ) {
        Text(
            text = "School or University name",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            text = "Degree, Field name: Master degree, Computer Science",
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Normal),
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(height = 3.dp))

        Text(
            text = "Start date - End date: 20223 - 2024",
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Normal),
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun CertificationsSection(onEditCertificationClicked: () -> Unit) {
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
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.weight(weight = 1f))

            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = stringResource(R.string.add_certification_icon_desc_text),
                modifier = Modifier.size(size = 32.dp).clickable { onEditCertificationClicked() }
            )
        }

        CertificationItemRow()
    }
}

@Composable
fun CertificationItemRow() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp)
    ) {
        Text(
            text = "Certification name",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            text = "Issued by: Meta",
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Normal),
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(height = 3.dp))

        Text(
            text = "Issued date: Sep 20223",
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Normal),
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(height = 3.dp))

        Text(
            text = "Credential ID XXXXXXXX",
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Normal),
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun ProjectsSection(onEditProjectClicked: () -> Unit) {
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
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.weight(weight = 1f))

            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = stringResource(R.string.add_project_icon_desc_text),
                modifier = Modifier.size(size = 32.dp).clickable { onEditProjectClicked() }
            )
        }

        ProjectItemRow()
    }
}

@Composable
fun ProjectItemRow() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp)
    ) {
        Text(
            text = "Project name",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            text = "start date - end date",
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Normal),
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(height = 5.dp))

        Text(
            text = "Project description",
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Normal),
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 5,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(height = 10.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = R.drawable.ic_skills_icon),
                contentDescription = "Skills icon",
                modifier = Modifier.size(size = 14.dp)
            )

            Spacer(modifier = Modifier.width(5.dp))

            Text(
                text = "Skills: Android, Coroutines Kotlin and +6 skills",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Composable
fun SkillsSection(onEditSkillsClicked: () -> Unit) {
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
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.weight(weight = 1f))

            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = stringResource(R.string.add_skills_icon_desc_text),
                modifier = Modifier.size(size = 32.dp).clickable { onEditSkillsClicked() }
            )
        }

        Text(
            text = "Liste des compétences",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(horizontal = 15.dp)
        )


    }
}
