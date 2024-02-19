package com.example.hiretop.ui.screens.entreprise

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hiretop.R
import com.example.hiretop.ui.extras.DropdownListWords

@Composable
fun CreateOrEditJobOfferScreen() {
    val mContext = LocalContext.current
    val mWidth = LocalConfiguration.current.screenWidthDp.dp
    val maxLength = 3000

    var offerName by remember { mutableStateOf("") }
    var selectedLocationType by remember { mutableStateOf("") }
    var selectedJobType by remember { mutableStateOf("") }
    var requiredSkills by remember { mutableStateOf("") }
    var requiredEducationLevel by remember { mutableStateOf("") }
    var offerDescription by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .wrapContentSize()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(15.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = stringResource(R.string.create_job_offer_text),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth(1f)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Divider()

        Spacer(modifier = Modifier.height(15.dp))

        Text(
            text = stringResource(R.string.indicates_required_text),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
        )

        Spacer(modifier = Modifier.height(15.dp))

        OutlinedTextField(
            value = offerName,
            onValueChange = { offerName = it },
            label = {
                Text(
                    text = stringResource(R.string.required_name_text),
                    style = MaterialTheme.typography.bodyLarge,
                )
            },
            shape = MaterialTheme.shapes.small,
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(height = 15.dp))

        DropdownListWords(
            title = stringResource(R.string.location_type_text),
            items = stringArrayResource(id = R.array.location_type_list),
            onItemSelected = { selectedLocationType = it }
        )

        Spacer(modifier = Modifier.height(height = 15.dp))

        DropdownListWords(
            title = stringResource(R.string.employment_type_text),
            items = stringArrayResource(id = R.array.job_type_list),
            onItemSelected = { selectedJobType = it }
        )

        Spacer(modifier = Modifier.height(height = 15.dp))

        OutlinedTextField(
            value = requiredSkills,
            onValueChange = { requiredSkills = it },
            label = {
                Text(
                    text = stringResource(R.string.required_skills_text),
                    style = MaterialTheme.typography.bodyLarge,
                )
            },
            placeholder = {
                Text(
                    text = stringResource(R.string.skills_field_placeholder_text),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.outline
                )
            },
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(height = 15.dp))

        OutlinedTextField(
            value = requiredEducationLevel,
            onValueChange = { requiredEducationLevel = it },
            label = {
                Text(
                    text = stringResource(R.string.required_education_text),
                    style = MaterialTheme.typography.bodyLarge,
                )
            },
            placeholder = {
                Text(
                    text = stringResource(R.string.education_field_placeholder_text),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.outline
                )
            },
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(height = 15.dp))

        OutlinedTextField(
            value = offerDescription,
            onValueChange = { if (it.length <= maxLength) offerDescription = it },
            label = {
                Text(
                    text = stringResource(R.string.description_text),
                    style = MaterialTheme.typography.bodyLarge,
                )
            },
            supportingText = {
                Text(
                    text = "${offerDescription.length} / $maxLength",
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
                .height(height = mWidth * 0.8f)
        )

        Spacer(modifier = Modifier.height(height = 25.dp))

        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            OutlinedButton(
                modifier = Modifier
                    .height(50.dp)
                    .weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.primary
                ),
                border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.primary),
                shape = MaterialTheme.shapes.large,
                onClick = { TODO("Cancel operation") }
            ) {
                Text(
                    text = stringResource(R.string.cancel_text),
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
                onClick = { TODO("Save offer in local and on remote server") }
            ) {
                Text(
                    text = stringResource(R.string.save_button_text),
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 18.sp)
                )
            }
        }

    }

}