package com.example.hiretop.ui.screens.messaging

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Badge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.hiretop.R
import com.example.hiretop.utils.Utils.getFormattedDateOrHour

@Composable
fun ChatListScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(15.dp)
    ) {
        Text(
            text = "Discussions",
            style = MaterialTheme.typography.headlineMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.wrapContentWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        repeat(5) {
            ChatItemRow()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatItemRow() {
    val counter by remember { mutableStateOf((1..500).random()) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(horizontal = 5.dp, vertical = 5.dp)
            .clickable { TODO("Navigate to CandidateInteractionScreen") },
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context = LocalContext.current)
                .data("")
                .crossfade(true)
                .build(),
            contentDescription = "Sender profile picture",
            contentScale = ContentScale.Crop,
            error = painterResource(id = R.drawable.ai_profile_picture),
            placeholder = painterResource(id = R.drawable.ai_profile_picture),
            modifier = Modifier
                .size(60.dp)
                .padding(4.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.width(5.dp))

        Column(modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Nom de l'expéditeur",
                    style = MaterialTheme.typography.headlineSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.wrapContentWidth()
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Titre de l'emploi",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.wrapContentWidth()
                )

                Spacer(modifier = Modifier.weight(1f))

                if (counter > 0) {
                    Badge(
                        modifier = Modifier.size(30.dp),
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ) {
                        Text(
                            text = if (counter > 99) "+99" else "$counter",
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.align(alignment = Alignment.CenterVertically)
                        )
                    }
                }
            }
        }
    }
}