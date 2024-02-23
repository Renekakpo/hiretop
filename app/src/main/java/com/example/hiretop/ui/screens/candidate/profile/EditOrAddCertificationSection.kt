package com.example.hiretop.ui.screens.candidate.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hiretop.R
import com.example.hiretop.models.Certification
import com.example.hiretop.ui.extras.DropdownListWords
import com.example.hiretop.ui.extras.FailurePopup
import com.example.hiretop.utils.Utils

@Composable
fun EditOrAddCertificationSection(
    currentValue: Certification?,
    onSaveClicked: (Certification) -> Unit
) {
    val mContext = LocalContext.current
    val mWidth = LocalConfiguration.current.screenWidthDp.dp

    var requiredName by remember { mutableStateOf(currentValue?.name ?: "") }
    var requiredIssuingOrganization by remember { mutableStateOf(currentValue?.issuingOrganization ?: "") }
    var issuedMonth by remember { mutableStateOf(currentValue?.issueMonth ?: "") }
    var issuedYear by remember { mutableStateOf(currentValue?.issueYear ?: "") }
    var expirationMonth by remember { mutableStateOf(currentValue?.expireMonth ?: "") }
    var expirationYear by remember { mutableStateOf(currentValue?.expireYear ?: "") }
    var credentialID by remember { mutableStateOf(currentValue?.credentialID ?: "") }
    var credentialURL by remember { mutableStateOf(currentValue?.credentialURL ?: "") }
    var skills by remember { mutableStateOf(currentValue?.skills?.joinToString { ", " } ?: "") }
    var onErrorMessage by remember { mutableStateOf<String?>(null) }

    if (!onErrorMessage.isNullOrEmpty()) {
        FailurePopup(errorMessage = "$onErrorMessage", onDismiss = {
            onErrorMessage = null // Reset error message
        })
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(horizontal = 25.dp, vertical = 15.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        Text(
            text = stringResource(R.string.indicates_required_text),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
        )

        Spacer(modifier = Modifier.height(height = 20.dp))

        OutlinedTextField(
            value = requiredName,
            onValueChange = { requiredName = it },
            label = {
                Text(
                    text = stringResource(R.string.required_certification_name_text),
                    style = MaterialTheme.typography.bodyLarge,
                )
            },
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(height = 15.dp))

        OutlinedTextField(
            value = requiredIssuingOrganization,
            onValueChange = { requiredIssuingOrganization = it },
            label = {
                Text(
                    text = stringResource(R.string.issuing_organization_text),
                    style = MaterialTheme.typography.bodyLarge,
                )
            },
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(height = 15.dp))

        Column {
            Text(
                text = stringResource(R.string.optional_issued_date_text),
                style = MaterialTheme.typography.bodyLarge,
            )
            Row {
                DropdownListWords(
                    modifier = Modifier.width(width = mWidth * 0.45f),
                    title = "",
                    items = stringArrayResource(id = R.array.months_array),
                    onItemSelected = { issuedMonth = it }
                )

                Spacer(modifier = Modifier.width(15.dp))

                DropdownListWords(
                    modifier = Modifier.width(width = mWidth * 0.45f),
                    title = "",
                    items = Utils.getYearsList().toTypedArray(),
                    onItemSelected = { issuedYear = it }
                )
            }
        }

        Spacer(modifier = Modifier.height(height = 15.dp))

        Column {
            Text(
                text = stringResource(R.string.optional_expiration_date_text),
                style = MaterialTheme.typography.bodyLarge,
            )
            Row {
                DropdownListWords(
                    modifier = Modifier.width(width = mWidth * 0.45f),
                    title = "",
                    items = stringArrayResource(id = R.array.months_array),
                    onItemSelected = { expirationMonth = it }
                )

                Spacer(modifier = Modifier.width(15.dp))

                DropdownListWords(
                    modifier = Modifier.width(width = mWidth * 0.45f),
                    title = "",
                    items = Utils.getYearsList().toTypedArray(),
                    onItemSelected = { expirationYear = it }
                )
            }
        }

        Spacer(modifier = Modifier.height(height = 15.dp))

        OutlinedTextField(
            value = credentialID,
            onValueChange = { credentialID = it },
            label = {
                Text(
                    text = stringResource(R.string.optional_credential_id_text),
                    style = MaterialTheme.typography.bodyLarge,
                )
            },
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(height = 15.dp))

        OutlinedTextField(
            value = credentialURL,
            onValueChange = { credentialURL = it },
            label = {
                Text(
                    text = stringResource(R.string.optional_credential_url_text),
                    style = MaterialTheme.typography.bodyLarge,
                )
            },
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(height = 15.dp))

        OutlinedTextField(
            value = skills,
            onValueChange = { skills = it },
            label = {
                Text(
                    text = stringResource(id = R.string.optional_skills_text),
                    style = MaterialTheme.typography.bodyLarge,
                )
            },
            placeholder = {
                Text(
                    text = stringResource(R.string.skills_field_placeholder_text),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(height = 25.dp))

        Button(
            modifier = Modifier
                .width(width = mWidth * 0.7F)
                .height(50.dp)
                .padding(horizontal = 15.dp)
                .align(alignment = Alignment.CenterHorizontally),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            shape = MaterialTheme.shapes.small,
            onClick = {
                if (requiredName.isEmpty()) {
                    onErrorMessage =
                        mContext.getString(R.string.empty_certification_title_error_text)
                } else if (requiredIssuingOrganization.isEmpty()) {
                    onErrorMessage =
                        mContext.getString(R.string.empty_issuing_organization_title_error_text)
                } else {
                    val certification = Certification(
                        name = requiredName,
                        issuingOrganization = requiredIssuingOrganization,
                        issueMonth = issuedMonth,
                        issueYear = issuedYear,
                        expireMonth = expirationMonth,
                        expireYear = expirationYear,
                        credentialID = credentialID,
                        credentialURL = credentialURL,
                        skills = skills.split(",").toSet()
                    )

                    onSaveClicked(certification)
                }
            }
        ) {
            Text(
                text = stringResource(R.string.save_button_text),
                style = MaterialTheme.typography.titleLarge.copy(fontSize = 18.sp)
            )
        }
    }
}