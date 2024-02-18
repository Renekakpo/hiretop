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
import androidx.compose.material3.Checkbox
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hiretop.R
import com.example.hiretop.ui.extras.DropdownListWords
import com.example.hiretop.utils.Utils.getYearsList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditOrAddExperienceSection(onSaveClicked: () -> Unit) {
    val mContext = LocalContext.current
    val mWidth = LocalConfiguration.current.screenWidthDp.dp
    val maxLength = 300

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var headline by remember { mutableStateOf("") }
    var selectedJobType by remember { mutableStateOf("") }
    var isCurrentPosition by remember { mutableStateOf(false) }

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
            value = firstName,
            onValueChange = { firstName = it },
            label = {
                Text(
                    text = stringResource(R.string.job_title_text),
                    style = MaterialTheme.typography.bodyLarge,
                )
            },
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(height = 15.dp))

        DropdownListWords(
            title = stringResource(R.string.employment_type_text),
            items = stringArrayResource(id = R.array.job_type_list),
            onItemSelected = { selectedJobType = it}
        )

        Spacer(modifier = Modifier.height(height = 15.dp))

        OutlinedTextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = {
                Text(
                    text = stringResource(R.string.company_name_text),
                    style = MaterialTheme.typography.bodyLarge,
                )
            },
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(height = 15.dp))

        OutlinedTextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = {
                Text(
                    text = stringResource(R.string.location_text),
                    style = MaterialTheme.typography.bodyLarge,
                )
            },
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(height = 15.dp))

        DropdownListWords(
            title = stringResource(R.string.location_type_text),
            items = stringArrayResource(id = R.array.location_type_list),
            onItemSelected = { selectedJobType = it}
        )

        Spacer(modifier = Modifier.height(height = 15.dp))

        Column {
            Text(
                text = stringResource(R.string.required_start_date_text),
                style = MaterialTheme.typography.bodyLarge,
            )
            Row {
                DropdownListWords(
                    modifier = Modifier.width(width = mWidth * 0.45f),
                    title = "",
                    items = stringArrayResource(id = R.array.months_array),
                    onItemSelected = { selectedJobType = it}
                )

                Spacer(modifier = Modifier.width(15.dp))

                DropdownListWords(
                    modifier = Modifier.width(width = mWidth * 0.45f),
                    title = "",
                    items = getYearsList().toTypedArray(),
                    onItemSelected = { selectedJobType = it}
                )
            }
        }

        Spacer(modifier = Modifier.height(height = 15.dp))

        Column {
            Text(
                text = stringResource(R.string.end_date_text),
                style = MaterialTheme.typography.bodyLarge,
            )
            Row {
                DropdownListWords(
                    modifier = Modifier.width(width = mWidth * 0.45f),
                    title = "",
                    items = stringArrayResource(id = R.array.months_array),
                    onItemSelected = { selectedJobType = it}
                )

                Spacer(modifier = Modifier.width(15.dp))

                DropdownListWords(
                    modifier = Modifier.width(width = mWidth * 0.45f),
                    title = "",
                    items = getYearsList().toTypedArray(),
                    onItemSelected = { selectedJobType = it}
                )
            }
        }

        Spacer(modifier = Modifier.height(height = 15.dp))

        OutlinedTextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = {
                Text(
                    text = stringResource(R.string.industry_text),
                    style = MaterialTheme.typography.bodyLarge,
                )
            },
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(height = 15.dp))

        OutlinedTextField(
            value = headline,
            onValueChange = { if (it.length <= maxLength) headline = it },
            label = {
                Text(
                    text = stringResource(R.string.description_text),
                    style = MaterialTheme.typography.bodyLarge,
                )
            },
            supportingText = {
                Text(
                    text = "${headline.length} / $maxLength",
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
                .height(height = mWidth * 0.4f)
        )

        Spacer(modifier = Modifier.height(height = 15.dp))

        OutlinedTextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = {
                Text(
                    text = stringResource(R.string.optional_skills_text),
                    style = MaterialTheme.typography.bodyLarge,
                )
            },
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(height = 25.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {

            Checkbox(checked = isCurrentPosition, onCheckedChange = { isCurrentPosition = it })

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = stringResource(R.string.current_job_text),
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp),
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        
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
            onClick = { onSaveClicked() }
        ) {
            Text(
                text = stringResource(R.string.save_button_text),
                style = MaterialTheme.typography.titleLarge.copy(fontSize = 18.sp)
            )
        }
    }
}