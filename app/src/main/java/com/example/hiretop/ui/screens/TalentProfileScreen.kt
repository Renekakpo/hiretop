package com.example.hiretop.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.hiretop.R

@Composable
fun TalentProfileScreen() {
    val mContext = LocalContext.current
    val mWidth = LocalConfiguration.current.screenWidthDp.dp

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        HeaderSection(width = mWidth)

        Spacer(modifier = Modifier.height(height = 15.dp))

        AboutSection()

        Spacer(modifier = Modifier.height(height = 15.dp))

        ExperienceSection()

        Spacer(modifier = Modifier.height(height = 15.dp))

        EducationSection()

        Spacer(modifier = Modifier.height(height = 15.dp))

        CertificationsSection()

        Spacer(modifier = Modifier.height(height = 15.dp))

        ProjectsSection()

        Spacer(modifier = Modifier.height(height = 15.dp))

        SkillsSection()

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
fun AboutSection() {
    Column(modifier = Modifier.fillMaxWidth().padding(top = 10.dp)) {
        Divider(modifier = Modifier.padding(horizontal = 15.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "A propos",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.weight(weight = 1f))

            Icon(
                painter = painterResource(id = R.drawable.ic_mode_edit_outline_24),
                contentDescription = "Edit icon"
            )
        }

        Text(
            text = "User description on 10 lines",
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
fun ExperienceSection() {
    Column(modifier = Modifier.fillMaxWidth().padding(top = 10.dp)) {
        Divider(modifier = Modifier.padding(horizontal = 15.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Experience",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.weight(weight = 1f))

            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add experience",
                modifier = Modifier.size(size = 32.dp)
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
fun EducationSection() {
    Column(modifier = Modifier.fillMaxWidth().padding(top = 10.dp)) {
        Divider(modifier = Modifier.padding(horizontal = 15.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Education",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.weight(weight = 1f))

            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add experience",
                modifier = Modifier.size(size = 32.dp)
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
fun CertificationsSection() {
    Column(modifier = Modifier.fillMaxWidth().padding(top = 10.dp)) {
        Divider(modifier = Modifier.padding(horizontal = 15.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Certifications",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.weight(weight = 1f))

            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add experience",
                modifier = Modifier.size(size = 32.dp)
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
fun ProjectsSection() {
    Column(modifier = Modifier.fillMaxWidth().padding(top = 10.dp)) {
        Divider(modifier = Modifier.padding(horizontal = 15.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Projets",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.weight(weight = 1f))

            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add experience",
                modifier = Modifier.size(size = 32.dp)
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
fun SkillsSection() {
    Column(modifier = Modifier.fillMaxWidth().padding(top = 10.dp)) {
        Divider(modifier = Modifier.padding(horizontal = 15.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Compétences",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.weight(weight = 1f))

            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add experience",
                modifier = Modifier.size(size = 32.dp)
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

