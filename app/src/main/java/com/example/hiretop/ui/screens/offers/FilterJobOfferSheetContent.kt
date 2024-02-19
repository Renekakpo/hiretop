package com.example.hiretop.ui.screens.offers

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.hiretop.R

@Composable
fun FilterSheetContent(
    onApplyFilter: () -> Unit,
    onResetFilter: () -> Unit
) {
    var selectedJobTypes by rememberSaveable { mutableStateOf(setOf<String>()) }
    var selectedEducations by remember { mutableStateOf(setOf<String>()) }
    var selectedLocationTypes by remember { mutableStateOf(setOf<String>()) }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = stringResource(R.string.filter_job_offers_text),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.job_type_text),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        CheckboxList(
            stringArrayResource(id = R.array.job_types).toList(),
            selectedJobTypes
        ) { jobType ->
            selectedJobTypes = if (selectedJobTypes.contains(jobType)) {
                selectedJobTypes - jobType
            } else {
                selectedJobTypes + jobType
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(id = R.string.education_text),
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        CheckboxList(
            stringArrayResource(id = R.array.education_levels).toList(),
            selectedEducations
        ) { education ->
            selectedEducations = if (selectedEducations.contains(education)) {
                selectedEducations - education
            } else {
                selectedEducations + education
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(id = R.string.location_type_text),
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        CheckboxList(
            stringArrayResource(id = R.array.location_type_list).drop(1).toList(),
            selectedLocationTypes
        ) { skill ->
            selectedLocationTypes = if (selectedLocationTypes.contains(skill)) {
                selectedLocationTypes - skill
            } else {
                selectedLocationTypes + skill
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            OutlinedButton(
                onClick = {
                    selectedEducations = emptySet()
                    selectedJobTypes = emptySet()
                    selectedLocationTypes = emptySet()
                    onResetFilter()
                },
                border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.primary),
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            ) {
                Text(
                    text = stringResource(R.string.reset_filter_text),
                    style = MaterialTheme.typography.titleMedium
                )
            }
            Button(
                onClick = { onApplyFilter() },
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
            ) {
                Text(
                    text = stringResource(R.string.apply_filter_text),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Composable
fun CheckboxList(
    items: List<String>,
    selectedItems: Set<String>,
    onItemSelected: (String) -> Unit
) {
    items.forEach { item ->
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { onItemSelected(item) }
        ) {
            Checkbox(
                checked = selectedItems.contains(item),
                onCheckedChange = { onItemSelected(item) },
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(
                text = item,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}