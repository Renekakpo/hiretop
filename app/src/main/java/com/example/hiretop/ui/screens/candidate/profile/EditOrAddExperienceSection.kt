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
import androidx.compose.material3.Checkbox
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
import com.example.hiretop.models.Experience
import com.example.hiretop.ui.extras.DropdownListWords
import com.example.hiretop.ui.extras.FailurePopup
import com.example.hiretop.utils.Utils.getYearsList

@Composable
fun EditOrAddExperienceSection(
    currentValue: Experience?,
    onSaveClicked: (Experience) -> Unit,
    onDeleteClicked: (Experience) -> Unit
) {
    val mContext = LocalContext.current
    val mWidth = LocalConfiguration.current.screenWidthDp.dp
    val maxLength = 300

    var requiredJobTitle by remember { mutableStateOf(currentValue?.title ?: "") }
    var selectedJobType by remember { mutableStateOf(currentValue?.employmentType ?: "") }
    var requiredCompanyName by remember { mutableStateOf(currentValue?.companyName ?: "") }
    var location by remember { mutableStateOf(currentValue?.location ?: "") }
    var selectedLocationType by remember { mutableStateOf(currentValue?.locationType ?: "") }
    var selectedStartMonth by remember { mutableStateOf(currentValue?.startMonth ?: "") }
    var selectedStartYear by remember { mutableStateOf(currentValue?.startYear ?: "") }
    var selectedEndMonth by remember { mutableStateOf(currentValue?.endMonth ?: "") }
    var selectedEndYear by remember { mutableStateOf(currentValue?.endYear ?: "") }
    var requiredIndustry by remember { mutableStateOf(currentValue?.industry ?: "") }
    var description by remember { mutableStateOf(currentValue?.description ?: "") }
    var skills by remember { mutableStateOf(currentValue?.skills?.joinToString(separator = ",")) }
    var isCurrentPosition by remember { mutableStateOf(currentValue?.isCurrentWork ?: false) }

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
            value = requiredJobTitle,
            onValueChange = { requiredJobTitle = it },
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
            currentItemIndex = if (selectedJobType.isNotEmpty()) stringArrayResource(id = R.array.job_type_list).indexOf(selectedJobType) else 0,
            onItemSelected = { selectedJobType = it }
        )

        Spacer(modifier = Modifier.height(height = 15.dp))

        OutlinedTextField(
            value = requiredCompanyName,
            onValueChange = { requiredCompanyName = it },
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
            value = location,
            onValueChange = { location = it },
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
            currentItemIndex = if (selectedLocationType.isNotEmpty()) stringArrayResource(id = R.array.location_type_list).indexOf(selectedLocationType) else 0,
            onItemSelected = { selectedLocationType = it }
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
                    currentItemIndex = if (selectedStartMonth.isNotEmpty()) stringArrayResource(id = R.array.months_array).indexOf(selectedStartMonth) else 0,
                    onItemSelected = { selectedStartMonth = it }
                )

                Spacer(modifier = Modifier.width(15.dp))

                DropdownListWords(
                    modifier = Modifier.width(width = mWidth * 0.45f),
                    title = "",
                    items = getYearsList().toTypedArray(),
                    currentItemIndex = if (selectedStartYear.isNotEmpty()) getYearsList().indexOf(selectedStartYear) else 0,
                    onItemSelected = { selectedStartYear = it }
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
                    currentItemIndex = if (selectedEndMonth.isNotEmpty()) stringArrayResource(id = R.array.months_array).indexOf(selectedEndMonth) else 0,
                    onItemSelected = { selectedEndMonth = it }
                )

                Spacer(modifier = Modifier.width(15.dp))

                DropdownListWords(
                    modifier = Modifier.width(width = mWidth * 0.45f),
                    title = "",
                    items = getYearsList().toTypedArray(),
                    currentItemIndex = if (selectedEndYear.isNotEmpty()) getYearsList().indexOf(selectedEndYear) else 0,
                    onItemSelected = { selectedEndYear = it }
                )
            }
        }

        Spacer(modifier = Modifier.height(height = 15.dp))

        OutlinedTextField(
            value = requiredIndustry,
            onValueChange = { requiredIndustry = it },
            label = {
                Text(
                    text = stringResource(R.string.required_industry_text),
                    style = MaterialTheme.typography.bodyLarge,
                )
            },
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(height = 15.dp))

        OutlinedTextField(
            value = description,
            onValueChange = { if (it.length <= maxLength) description = it },
            label = {
                Text(
                    text = stringResource(R.string.description_text),
                    style = MaterialTheme.typography.bodyLarge,
                )
            },
            supportingText = {
                Text(
                    text = "${description.length} / $maxLength",
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
            value = skills ?: "",
            onValueChange = { skills = it },
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

        Row(verticalAlignment = Alignment.CenterVertically) {
            Button(
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp)
                    .padding(horizontal = 5.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError,
                    disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                ),
                enabled = currentValue != null,
                shape = MaterialTheme.shapes.small,
                onClick = {
                    if (currentValue != null) {
                        onDeleteClicked(currentValue)
                    }
                }
            ) {
                Text(
                    text = stringResource(R.string.delete_button_text),
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 18.sp)
                )
            }

            Button(
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp)
                    .padding(horizontal = 5.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                shape = MaterialTheme.shapes.small,
                onClick = {
                    val experience: Experience?
                    if (requiredJobTitle.isEmpty()) {
                        onErrorMessage = mContext.getString(R.string.experience_title_error_text)
                    } else if (requiredCompanyName.isEmpty()) {
                        onErrorMessage =
                            mContext.getString(R.string.experience_company_name_error_text)
                    } else if (requiredIndustry.isEmpty()) {
                        onErrorMessage =
                            mContext.getString(R.string.experience_industry_title_error_text)
                    } else if (selectedStartMonth.isEmpty()) {
                        onErrorMessage =
                            mContext.getString(R.string.experience_start_month_error_text)
                    } else if (selectedStartYear.isEmpty()) {
                        onErrorMessage =
                            mContext.getString(R.string.experience_start_year_error_text)
                    } else if (selectedEndMonth.isEmpty()) {
                        onErrorMessage =
                            mContext.getString(R.string.experience_end_month_error_text)
                    } else if (selectedEndYear.isEmpty()) {
                        onErrorMessage = mContext.getString(R.string.experience_end_year_error_text)
                    } else {
                        experience = Experience(
                            title = requiredJobTitle,
                            employmentType = selectedJobType,
                            companyName = requiredCompanyName,
                            location = location,
                            locationType = selectedLocationType,
                            isCurrentWork = isCurrentPosition,
                            startMonth = selectedStartMonth,
                            startYear = selectedStartYear,
                            endMonth = selectedEndMonth,
                            endYear = selectedEndYear,
                            industry = requiredIndustry,
                            description = description,
                            skills = if (skills.isNullOrEmpty()) null else skills?.split(",")
                        )
                        onSaveClicked(experience)
                    }
                }
            ) {
                Text(
                    text = stringResource(R.string.save_button_text),
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 18.sp),
                    modifier = Modifier.align(alignment = Alignment.CenterVertically)
                )
            }
        }
    }
}