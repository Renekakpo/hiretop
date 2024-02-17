package com.example.hiretop.ui.screens.profile

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
import com.example.hiretop.ui.extras.DropdownListWords
import com.example.hiretop.utils.Utils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditOrAddCertificationSection() {
    val mContext = LocalContext.current
    val mWidth = LocalConfiguration.current.screenWidthDp.dp

    var name by remember { mutableStateOf("") }
    var issuingOrganization by remember { mutableStateOf("") }
    var issuedMonth by remember { mutableStateOf("") }
    var issuedYear by remember { mutableStateOf("") }
    var expirationMonth by remember { mutableStateOf("") }
    var expirationYear by remember { mutableStateOf("") }
    var credentialID by remember { mutableStateOf("") }
    var credentialURL by remember { mutableStateOf("") }
    var skills by remember { mutableStateOf("") }

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
            value = name,
            onValueChange = { name = it },
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
            value = issuingOrganization,
            onValueChange = { issuingOrganization = it },
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
                    onItemSelected = { issuedMonth = it}
                )

                Spacer(modifier = Modifier.width(15.dp))

                DropdownListWords(
                    modifier = Modifier.width(width = mWidth * 0.45f),
                    title = "",
                    items = Utils.getYearsList().toTypedArray(),
                    onItemSelected = { issuedYear = it}
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
                    onItemSelected = { expirationMonth = it}
                )

                Spacer(modifier = Modifier.width(15.dp))

                DropdownListWords(
                    modifier = Modifier.width(width = mWidth * 0.45f),
                    title = "",
                    items = Utils.getYearsList().toTypedArray(),
                    onItemSelected = { expirationYear = it}
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
                    text = stringResource(R.string.optional_skills_field_placeholder_text),
                    style = MaterialTheme.typography.bodyLarge,
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
            onClick = { onSaveButtonClicked() }
        ) {
            Text(
                text = stringResource(R.string.save_button_text),
                style = MaterialTheme.typography.titleLarge.copy(fontSize = 18.sp)
            )
        }
    }
}

private fun onSaveButtonClicked() {
    TODO("Not yet implemented")
}