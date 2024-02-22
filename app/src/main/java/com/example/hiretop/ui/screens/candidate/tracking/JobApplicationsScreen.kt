package com.example.hiretop.ui.screens.candidate.tracking

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Badge
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.hiretop.R
import com.example.hiretop.models.JobApplication
import com.example.hiretop.models.jobApplicationsLists
import com.example.hiretop.utils.Utils.formatDate
import com.example.hiretop.utils.Utils.getAppliedTimeAgo

@Composable
fun JobApplicationsScreen(modifier: Modifier = Modifier) {
    val mContext = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(top = 15.dp, start = 15.dp, end = 15.dp)
    ) {
        Text(
            text = stringResource(R.string.application_tracking_text),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp)
        )

        Spacer(modifier = Modifier.height(25.dp))

        jobApplicationsLists.groupBy { it.status }.forEach { (status, applications) ->
            var expanded by remember { mutableStateOf(false) }

            ExpandableSection(
                title = status,
                expanded = expanded,
                counter = applications.size,
                onExpandToggle = { expanded = it }
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(applications) { item ->
                        JobApplicationItem(context = mContext, jobApplication = item)
                        Spacer(modifier = Modifier.height(10.dp))  // Add a divider between each item
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun JobApplicationItem(context: Context, jobApplication: JobApplication) {
    // You can customize the appearance based on the status of the job application
    val statusColor = when (jobApplication.status) {
        stringResource(R.string.on_hold_application_status_text) -> MaterialTheme.colorScheme.inverseOnSurface
        stringResource(R.string.on_going_application_status_text) -> MaterialTheme.colorScheme.inversePrimary
        stringResource(R.string.approved_application_status_text) -> MaterialTheme.colorScheme.primary
        stringResource(R.string.declined_application_status_text) -> MaterialTheme.colorScheme.error
        else -> MaterialTheme.colorScheme.onBackground
    }

    val statusContentColor = when (jobApplication.status) {
        stringResource(R.string.on_hold_application_status_text) -> MaterialTheme.colorScheme.onBackground
        stringResource(R.string.on_going_application_status_text) -> MaterialTheme.colorScheme.onPrimary
        stringResource(R.string.approved_application_status_text) -> MaterialTheme.colorScheme.onPrimary
        stringResource(R.string.declined_application_status_text) -> MaterialTheme.colorScheme.onError
        else -> MaterialTheme.colorScheme.background
    }

    Column(
        modifier = Modifier
            .wrapContentSize()
            .background(color = statusColor, shape = RoundedCornerShape(CornerSize(8.dp)))
            .padding(15.dp)
    ) {
        Text(
            text = jobApplication.jobOfferTitle,
            color = statusContentColor,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(15.dp))

        Text(
            text = jobApplication.companyName,
            color = statusContentColor,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(7.dp))

        Text(
            text = "${jobApplication.locationType} | ${jobApplication.location}",
            color = statusContentColor,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(7.dp))

        if (jobApplication.status == stringResource(R.string.approved_application_status_text) && jobApplication.interviewDate != null) {
            Text(
                text = stringResource(
                    R.string.interview_date_text,
                    formatDate(jobApplication.interviewDate)
                ),
                color = statusContentColor,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(7.dp))

        if (jobApplication.status == stringResource(R.string.approved_application_status_text) && jobApplication.offerReceived) {
            Text(
                text = stringResource(R.string.offer_received_text),
                color = statusContentColor,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = getAppliedTimeAgo(context, jobApplication.appliedAt),
            color = statusContentColor,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpandableSection(
    title: String,
    expanded: Boolean,
    counter: Int,
    onExpandToggle: (Boolean) -> Unit,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                border = BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onBackground
                ), shape = RoundedCornerShape(CornerSize(8.dp))
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 15.dp)
                .clickable { onExpandToggle(!expanded) }
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.wrapContentSize()
            )

            Spacer(modifier = Modifier.weight(1f))

            Badge(
                modifier = Modifier.size(33.dp),
                containerColor = MaterialTheme.colorScheme.inversePrimary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Text(
                    text = if (counter > 99) "+99" else "$counter",
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.align(alignment = Alignment.CenterVertically)
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Icon(
                imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.wrapContentSize()
            )
        }

        if (expanded) {
            content()
        }
    }
}