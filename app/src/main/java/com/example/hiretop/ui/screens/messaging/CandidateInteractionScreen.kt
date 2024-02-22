package com.example.hiretop.ui.screens.messaging

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.hiretop.R
import com.example.hiretop.models.Message
import com.example.hiretop.models.mockMessages
import com.example.hiretop.utils.toHourMinuteString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CandidateInteractionScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.wrapContentSize(),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
                navigationIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                        contentDescription = stringResource(id = R.string.back_arrow_icon_desc_text),
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.clickable { TODO("Navigate back to previous screen") }
                    )
                },
                title = {
                    TopBarContent()
                }
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background)
                .padding(innerPadding)
        ) {

            MessageList(messages = mockMessages)

            Spacer(modifier = Modifier.weight(1f))

            NewMessageForm()

            Spacer(modifier = Modifier.height(height = 10.dp))
        }
    }
}

@Composable
fun TopBarContent() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
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
            Text(
                text = "Nom du destinataire",
                style = MaterialTheme.typography.headlineSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.wrapContentWidth()
            )

            Text(
                text = "Titre de l'emploi",
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.wrapContentWidth()
            )
        }
    }
}

@Composable
fun NewMessageForm() {
    val mHeight = LocalConfiguration.current.screenHeightDp.dp
    var messageText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp)
    ) {
        OutlinedTextField(
            value = messageText,
            onValueChange = { messageText = it },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = mHeight * 0.4f),
            placeholder = {
                Text(
                    text = stringResource(R.string.message_field_placeholder),
                    style = MaterialTheme.typography.bodyLarge
                )
            },
            shape = MaterialTheme.shapes.extraLarge,
            trailingIcon = {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.Send,
                    contentDescription = stringResource(R.string.send_icon_desc_text),
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable { TODO("Send message") }
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                disabledBorderColor = MaterialTheme.colorScheme.primary,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.primary
            )
        )
    }
}

@Composable
fun MessageList(messages: List<Message>) {
    LazyColumn(
        modifier = Modifier
            .wrapContentSize()
            .padding(16.dp)
    ) {
        items(messages) { message ->
            MessageItem(message = message)

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun MessageItem(message: Message) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = if (message.to.lowercase()
                .contains("sender_id_1")
        ) Arrangement.End else Arrangement.Start
    ) {
        Column(
            modifier = Modifier
                .wrapContentSize()
                .background(
                    color = MaterialTheme.colorScheme.inversePrimary,
                    shape = MaterialTheme.shapes.small
                )
                .padding(8.dp)
        ) {
            Text(
                text = message.content,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = message.createdAt.toHourMinuteString(),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.align(alignment = Alignment.End)
            )
        }
    }
}