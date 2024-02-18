package com.example.hiretop.ui.screens.offers

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hiretop.R
import com.example.hiretop.models.JobOffer
import com.example.hiretop.models.generateFakeJobOffers
import com.example.hiretop.utils.Utils.getTimeAgo

@Composable
fun JobOffersScreen() {
    val jobOffers = generateFakeJobOffers(10)
    var searchInput by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(15.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = searchInput,
                onValueChange = { searchInput = it },
                textStyle = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp),
                placeholder = {
                    Text(
                        text = stringResource(R.string.job_search_field_placeholder_text),
                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp),
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Search,
                        contentDescription = stringResource(R.string.search_offer_icon_desc_text),
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(3.dp)
                    )
                },
                shape = MaterialTheme.shapes.medium,
                singleLine = true,
                maxLines = 1,
                modifier = Modifier
                    .weight(1f)
                    .height(height = 60.dp)
            )

            Box(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(5.dp)
                    .weight(0.2f)
                    .background(
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(corner = CornerSize(8.dp))
                    )
                    .clickable { onFilterClicked() }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_filter_black_icon),
                    contentDescription = stringResource(R.string.edit_icon_desc_text),
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(size = 45.dp)
                        .padding(5.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(height = 15.dp))

        Divider()

        Spacer(modifier = Modifier.height(height = 15.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(15.dp)) {
            itemsIndexed(jobOffers) { _, item ->
                JobOfferItemRow(
                    jobOffer = item,
                    onJobOfferClicked = { /* TODO: Navigate to JobOfferDetails screen with jobOffer as param */ })
            }
        }
    }
}

@Composable
fun JobOfferItemRow(jobOffer: JobOffer, onJobOfferClicked: (JobOffer) -> Unit) {
    Column(
        modifier = Modifier
            .wrapContentSize()
            .background(
                color = MaterialTheme.colorScheme.background
            )
            .border(
                border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.onBackground),
                shape = RoundedCornerShape(CornerSize(8.dp))
            )
            .padding(15.dp)
            .clickable { onJobOfferClicked(jobOffer) }
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = jobOffer.title,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.weight(0.1f))

            Icon(
                imageVector = Icons.Outlined.BookmarkBorder,
                contentDescription = stringResource(R.string.bookmark_offer_icon_desc),
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .size(34.dp)
                    .padding(3.dp)
                    .clickable { onBookmarkOfferClicked() }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = jobOffer.company,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(5.dp))

        Text(
            text = jobOffer.locationType,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(15.dp))

        Text(
            text = jobOffer.description,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
            maxLines = 4,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = getTimeAgo(jobOffer.postedAt),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            maxLines = 4,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))
    }
}

private fun onBookmarkOfferClicked() {
    TODO("Not yet implemented")
}

private fun onFilterClicked() {
    TODO("Not yet implemented")
}
