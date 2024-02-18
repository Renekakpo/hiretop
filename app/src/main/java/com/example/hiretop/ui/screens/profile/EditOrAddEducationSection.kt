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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hiretop.R
import com.example.hiretop.ui.extras.DropdownListWords
import com.example.hiretop.utils.Utils

@Composable
fun EditOrAddEducationSection(onSaveClicked: () -> Unit) {
    val mContext = LocalContext.current
    val mWidth = LocalConfiguration.current.screenWidthDp.dp
    val descMaxLength = 1000
    val actMaxLength = 500

    var schoolName by remember { mutableStateOf("") }
    var degree by remember { mutableStateOf("") }
    var fieldOfStudy by remember { mutableStateOf("") }
    var grade by remember { mutableStateOf("") }
    var activities by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var startMonth by remember { mutableStateOf("") }
    var startYear by remember { mutableStateOf("") }
    var endMonth by remember { mutableStateOf("") }
    var endYear by remember { mutableStateOf("") }

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
            value = schoolName,
            onValueChange = { schoolName = it },
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
                    onItemSelected = { startMonth = it}
                )

                Spacer(modifier = Modifier.width(15.dp))

                DropdownListWords(
                    modifier = Modifier.width(width = mWidth * 0.45f),
                    title = "",
                    items = Utils.getYearsList().toTypedArray(),
                    onItemSelected = { startYear = it}
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
                    onItemSelected = { endMonth = it}
                )

                Spacer(modifier = Modifier.width(15.dp))

                DropdownListWords(
                    modifier = Modifier.width(width = mWidth * 0.45f),
                    title = "",
                    items = Utils.getYearsList().toTypedArray(),
                    onItemSelected = { endYear = it}
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
            onClick = { onSaveClicked() }
        ) {
            Text(
                text = stringResource(R.string.save_button_text),
                style = MaterialTheme.typography.titleLarge.copy(fontSize = 18.sp)
            )
        }
    }
}