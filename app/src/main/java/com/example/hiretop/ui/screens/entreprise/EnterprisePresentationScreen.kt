package com.example.hiretop.ui.screens.entreprise

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.outlined.Edit
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.hiretop.R
import com.example.hiretop.ui.extras.HireTopBottomSheet
import com.example.hiretop.utils.Utils

@Composable
fun EnterprisePresentationScreen() {
    val mContext = LocalContext.current
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
        HeaderSection(onEditHeaderClicked = {
            sheetTitle = mContext.getString(R.string.modify_summary_text)
            bottomSheetContent = {
                EditProfileHeaderSection(onSaveClicked = {
                    showBottomSheet = false
                })
            }
            showBottomSheet = true
        })

        Spacer(modifier = Modifier.height(height = 20.dp))

        AboutSection(onEditAboutClicked = {
            sheetTitle = mContext.getString(R.string.optional_about_text)
            bottomSheetContent = {
                EditEnterpriseAboutSection(onSaveClicked = {
                    showBottomSheet = false
                })
            }
            showBottomSheet = true
        })

        Spacer(modifier = Modifier.height(height = 20.dp))

        CultureAndValuesSection(onEditCultureAndValuesClicked = {
            sheetTitle = mContext.getString(R.string.enterprise_culture_values_text)
            bottomSheetContent = {
                EditEnterpriseCultureAndValuesSection(onSaveClicked = {
                    showBottomSheet = false
                })
            }
            showBottomSheet = true
        })

        Spacer(modifier = Modifier.height(height = 20.dp))

        EnterpriseContactDetailsSection(onEditContactDetailsClicked = {
            sheetTitle = mContext.getString(R.string.enterprise_contact_details_text)
            bottomSheetContent = {
                EditEnterpriseContactDetailsSection(onSaveClicked = {
                    showBottomSheet = false
                })
            }
            showBottomSheet = true
        })

        Spacer(modifier = Modifier.height(height = 15.dp))
    }
}

@Composable
private fun HeaderSection(onEditHeaderClicked: () -> Unit) {
    val context = LocalContext.current
    var requestCode = 0
    val mWidth = LocalConfiguration.current.screenWidthDp.dp
    var bannerFilePath by remember { mutableStateOf("") }
    var profilePictureFilePath by remember { mutableStateOf("") }
    // Activity result launcher for picking an image from gallery
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { imageUri ->
        if (imageUri != null) {
            try {
                // Load and set the compressed image to the appropriate AsyncImage
                if (requestCode == 1) {
                    bannerFilePath =
                        Utils.compressImage(
                            context,
                            imageUri,
                            "compressed_banner.png"
                        )?.absolutePath ?: ""
                } else if (requestCode == 2) {
                    profilePictureFilePath =
                        Utils.compressImage(
                            context,
                            imageUri,
                            "compressed_profile_picture.png"
                        )?.absolutePath ?: ""
                }
            } catch (e: Exception) {
                // TODO: Handle exception
            }
        } else {
            // TODO: Handle error
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
                error = painterResource(id = R.drawable.user_profile_banner),
                placeholder = painterResource(id = R.drawable.user_profile_banner),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height = mWidth / 3.5F)
                    .align(alignment = Alignment.TopStart)
                    .clickable {
                        requestCode = 1
                        galleryLauncher.launch("image/*")
                    }
            )

            // Edit banner icon
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

            // Profile picture
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(profilePictureFilePath)
                    .crossfade(true)
                    .build(),
                contentDescription = stringResource(R.string.user_profile_picture_desc_text),
                contentScale = ContentScale.Crop,
                error = painterResource(id = R.drawable.ai_profile_picture),
                placeholder = painterResource(id = R.drawable.ai_profile_picture),
                modifier = Modifier
                    .size(150.dp)
                    .padding(4.dp)
                    .clip(CircleShape)
                    .align(alignment = Alignment.BottomStart)
                    .clickable {
                        requestCode = 2
                        galleryLauncher.launch("image/*")
                    }
            )

            // Add Icon
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

        Column(modifier = Modifier.clickable { onEditHeaderClicked() }) {
            Text(
                text = stringResource(R.string.enterprise_name_info),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(horizontal = 15.dp)
            )

            Text(
                text = stringResource(R.string.enterprise_headling_info),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(horizontal = 15.dp)
            )

            Text(
                text = stringResource(R.string.industry_and_location_info),
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
private fun AboutSection(onEditAboutClicked: () -> Unit) {
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

            Icon(
                imageVector = Icons.Outlined.Edit,
                contentDescription = stringResource(R.string.edit_icon_desc_text),
                modifier = Modifier.clickable { onEditAboutClicked() }
            )
        }

        Text(
            text = stringResource(R.string.enterprise_presentation_info),
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
private fun CultureAndValuesSection(onEditCultureAndValuesClicked: () -> Unit) {
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

            Icon(
                imageVector = Icons.Outlined.Edit,
                contentDescription = stringResource(R.string.edit_icon_desc_text),
                modifier = Modifier.clickable { onEditCultureAndValuesClicked() }
            )
        }

        Text(
            text = stringResource(R.string.enterprise_culture_values_info),
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
private fun EnterpriseContactDetailsSection(onEditContactDetailsClicked: () -> Unit) {
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

            Icon(
                imageVector = Icons.Outlined.Edit,
                contentDescription = stringResource(R.string.edit_icon_desc_text),
                modifier = Modifier.clickable { onEditContactDetailsClicked() }
            )
        }

        Text(
            text = stringResource(R.string.enterprise_contact_details_info),
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