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
import com.example.hiretop.models.Education
import com.example.hiretop.ui.extras.DropdownListWords
import com.example.hiretop.ui.extras.FailurePopup
import com.example.hiretop.utils.Utils

@Composable
fun EditOrAddEducationSection(currentValue: Education?,onSaveClicked: (Education) -> Unit) {
    val mContext = LocalContext.current
    val mWidth = LocalConfiguration.current.screenWidthDp.dp
    val descMaxLength = 1000
    val actMaxLength = 500

    var requiredSchoolName by remember { mutableStateOf(currentValue?.school ?: "") }
    var degree by remember { mutableStateOf(currentValue?.degree ?: "") }
    var fieldOfStudy by remember { mutableStateOf(currentValue?.fieldOfStudy ?: "") }
    var grade by remember { mutableStateOf(currentValue?.grade?: "") }
    var activities by remember { mutableStateOf(currentValue?.activities ?: "") }
    var description by remember { mutableStateOf(currentValue?.description ?: "") }
    var startMonth by remember { mutableStateOf(currentValue?.startMonth ?: "") }
    var startYear by remember { mutableStateOf(currentValue?.startYear ?: "") }
    var endMonth by remember { mutableStateOf(currentValue?.endMonth ?: "") }
    var endYear by remember { mutableStateOf(currentValue?.endYear ?: "") }
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
            value = requiredSchoolName,
            onValueChange = { requiredSchoolName = it },
            label = {
                Text(
                    text = stringResource(R.string.required_school_text),
                    style = MaterialTheme.typography.bodyLarge,
                )
            },
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(height = 15.dp))

        OutlinedTextField(
            value = degree,
            onValueChange = { degree = it },
            label = {
                Text(
                    text = stringResource(R.string.optional_degree_text),
                    style = MaterialTheme.typography.bodyLarge,
                )
            },
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(height = 15.dp))

        OutlinedTextField(
            value = fieldOfStudy,
            onValueChange = { fieldOfStudy = it },
            label = {
                Text(
                    text = stringResource(R.string.optional_field_of_study_text),
                    style = MaterialTheme.typography.bodyLarge,
                )
            },
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(height = 15.dp))

        Column {
            Text(
                text = stringResource(R.string.optional_start_date),
                style = MaterialTheme.typography.bodyLarge,
            )
            Row {
                DropdownListWords(
                    modifier = Modifier.width(width = mWidth * 0.45f),
                    title = "",
                    items = stringArrayResource(id = R.array.months_array),
                    onItemSelected = { startMonth = it }
                )

                Spacer(modifier = Modifier.width(15.dp))

                DropdownListWords(
                    modifier = Modifier.width(width = mWidth * 0.45f),
                    title = "",
                    items = Utils.getYearsList().toTypedArray(),
                    onItemSelected = { startYear = it }
                )
            }
        }

        Spacer(modifier = Modifier.height(height = 15.dp))

        Column {
            Text(
                text = stringResource(R.string.optional_end_or_planned_date_text),
                style = MaterialTheme.typography.bodyLarge,
            )
            Row {
                DropdownListWords(
                    modifier = Modifier.width(width = mWidth * 0.45f),
                    title = "",
                    items = stringArrayResource(id = R.array.months_array),
                    onItemSelected = { endMonth = it }
                )

                Spacer(modifier = Modifier.width(15.dp))

                DropdownListWords(
                    modifier = Modifier.width(width = mWidth * 0.45f),
                    title = "",
                    items = Utils.getYearsList().toTypedArray(),
                    onItemSelected = { endYear = it }
                )
            }
        }

        Spacer(modifier = Modifier.height(height = 15.dp))

        OutlinedTextField(
            value = grade,
            onValueChange = { grade = it },
            label = {
                Text(
                    text = stringResource(R.string.optional_grade_text),
                    style = MaterialTheme.typography.bodyLarge,
                )
            },
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(height = 15.dp))

        OutlinedTextField(
            value = activities,
            onValueChange = { if (it.length <= actMaxLength) activities = it },
            label = {
                Text(
                    text = stringResource(R.string.optional_activities_text),
                    style = MaterialTheme.typography.bodyLarge,
                )
            },
            supportingText = {
                Text(
                    text = "${activities.length} / $actMaxLength",
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
            value = description,
            onValueChange = { if (it.length <= descMaxLength) description = it },
            label = {
                Text(
                    text = stringResource(R.string.optional_description_text),
                    style = MaterialTheme.typography.bodyLarge,
                )
            },
            supportingText = {
                Text(
                    text = "${activities.length} / $descMaxLength",
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
                if (requiredSchoolName.isEmpty()) {
                    onErrorMessage = mContext.getString(R.string.empty_school_name_error_text)
                } else {
                    val education = Education(
                        school = requiredSchoolName,
                        degree = degree,
                        fieldOfStudy = fieldOfStudy,
                        grade = grade,
                        startMonth = startMonth,
                        startYear = startYear,
                        endMonth = endMonth,
                        endYear = endYear,
                        activities = activities,
                        description = description
                    )

                    onSaveClicked(education)
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