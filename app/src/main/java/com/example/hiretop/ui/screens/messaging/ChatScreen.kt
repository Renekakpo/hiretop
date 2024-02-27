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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.hiretop.R
import com.example.hiretop.models.ChatItem
import com.example.hiretop.models.ChatItemUI
import com.example.hiretop.models.Message
import com.example.hiretop.navigation.NavDestination
import com.example.hiretop.ui.extras.FailurePopup
import com.example.hiretop.ui.screens.candidate.profile.CandidateProfileScreen
import com.example.hiretop.utils.toHourMinuteString
import com.example.hiretop.viewModels.ChatViewModel

object ChatScreen : NavDestination {
    override val route: String = "chat_screen"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    navController: NavController,
    chatItemUI: ChatItemUI,
    chatViewModel: ChatViewModel = hiltViewModel()
) {
    val isEnterpriseAccount by chatViewModel.isEnterpriseAccount.collectAsState(initial = null)
    val enterpriseProfileId by chatViewModel.enterpriseProfileId.collectAsState(initial = null)
    val candidateProfileId by chatViewModel.candidateProfileId.collectAsState(initial = null)
    val messages by chatViewModel.messages.collectAsState()
    val jobApplication by chatViewModel.jobApplication.collectAsState()
    var onErrorMessage by remember { mutableStateOf<String?>(null) }
    var chatItemUIState by remember { mutableStateOf(chatItemUI) }

    if (!onErrorMessage.isNullOrEmpty()) {
        FailurePopup(errorMessage = "$onErrorMessage", onDismiss = {
            onErrorMessage = null
            if (messages.isNullOrEmpty()) {
                // Navigate back
                navController.navigateUp()
            }
        })
    }

    LaunchedEffect(chatViewModel) {
        if (!chatItemUIState.chatId.isNullOrEmpty()) { // Not the first time
            chatViewModel.getListOfMessages(
                chatId = chatItemUIState.chatId!!,
                onSuccess = { },
                onFailure = {
                    onErrorMessage = it
                }
            )

            // Observe new messages
            chatViewModel.observeNewMessageByChatId(
                chatId = chatItemUIState.chatId!!,
                onSuccess = {},
                onFailure = { onErrorMessage = it }
            )
        }

        chatViewModel.getJobApplicationFromChatItemUI(
            jobApplicationId = chatItemUIState.jobApplicationId,
            onSuccess = {},
            onFailure = {}
        )
    }

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
                        modifier = Modifier.clickable { navController.navigateUp() }
                    )
                },
                title = {
                    TopBarContent(
                        chatItemUI = chatItemUIState,
                        onTopBarClicked = {
                            val isPreviewMode = true
                            val profileId = candidateProfileId
                            navController.navigate(route = "${CandidateProfileScreen.route}/$isPreviewMode/$profileId")
                        }
                    )
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

            if (!messages.isNullOrEmpty()) {
                MessageList(
                    messages = messages!!,
                    currentProfileId = "${if (isEnterpriseAccount != null && isEnterpriseAccount == true) enterpriseProfileId else candidateProfileId}"
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            NewMessageForm(onSendMessageClicked = { input ->
                if (chatItemUIState.chatId.isNullOrEmpty()) { // New chat
                    var chatItem = ChatItem(
                        jobApplicationId = jobApplication?.jobApplicationId,
                        jobOfferId = jobApplication?.jobOfferId,
                        candidateProfileId = jobApplication?.candidateProfileId,
                        enterpriseProfileId = jobApplication?.enterpriseProfileId
                    )

                    chatViewModel.createOrEditChatItem(
                        chatItem = chatItem,
                        onSuccess = { chatId ->
                            chatItemUIState = chatItemUIState.copy(chatId = chatId)
                            chatItem = chatItem.copy(chatId = chatId)

                            val message = Message(
                                subject = chatId,
                                to = if (isEnterpriseAccount != null && isEnterpriseAccount == true)
                                    chatItem.candidateProfileId
                                else
                                    chatItem.enterpriseProfileId,
                                from = if (isEnterpriseAccount != null && isEnterpriseAccount == true)
                                    chatItem.enterpriseProfileId
                                else
                                    chatItem.candidateProfileId,
                                content = input,
                                received = false,
                                isRead = false,
                                createdAt = System.currentTimeMillis()
                            )

                            chatViewModel.sendMessage(
                                message = message,
                                onSuccess = { sentMessageId ->
                                    if (sentMessageId.isNotEmpty()) {

                                        chatViewModel.getListOfMessages(
                                            chatId = message.subject ?: "",
                                            onSuccess = {},
                                            onFailure = {}
                                        )
                                    }
                                },
                                onFailure = {
                                    onErrorMessage = it
                                }
                            )
                        },
                        onFailure = {
                            onErrorMessage = it
                        }
                    )
                } else {
                    val message = Message(
                        subject = chatItemUIState.chatId,
                        to = if (isEnterpriseAccount != null && isEnterpriseAccount == true)
                            jobApplication?.candidateProfileId
                        else
                            jobApplication?.enterpriseProfileId,
                        from = if (isEnterpriseAccount != null && isEnterpriseAccount == true)
                            jobApplication?.enterpriseProfileId
                        else
                            jobApplication?.candidateProfileId,
                        content = input,
                        received = false,
                        isRead = false,
                        createdAt = System.currentTimeMillis()
                    )

                    chatViewModel.sendMessage(
                        message = message,
                        onSuccess = { sentMessageId ->
                            if (sentMessageId.isNotEmpty()) {
                                chatViewModel.getListOfMessages(
                                    chatId = message.subject ?: "",
                                    onSuccess = {},
                                    onFailure = {}
                                )
                            }
                        },
                        onFailure = {
                            onErrorMessage = it
                        }
                    )
                }
            })

            Spacer(modifier = Modifier.height(height = 10.dp))
        }
    }
}

@Composable
fun TopBarContent(chatItemUI: ChatItemUI, onTopBarClicked: (ChatItemUI) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .clickable { onTopBarClicked(chatItemUI) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context = LocalContext.current)
                .data(chatItemUI.pictureUrl)
                .crossfade(true)
                .build(),
            contentDescription = stringResource(id = R.string.user_profile_picture_desc_text),
            contentScale = ContentScale.Crop,
            error = painterResource(id = R.drawable.user_profile_placeholder),
            placeholder = painterResource(id = R.drawable.user_profile_placeholder),
            modifier = Modifier
                .size(60.dp)
                .padding(4.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.width(5.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = if (chatItemUI.profileName.isNullOrEmpty()) stringResource(id = R.string.unknown_text) else chatItemUI.profileName,
                style = MaterialTheme.typography.headlineSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.wrapContentWidth()
            )

            Text(
                text = chatItemUI.offerTitle,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.wrapContentWidth()
            )
        }
    }
}

@Composable
fun NewMessageForm(onSendMessageClicked: (String) -> Unit) {
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
                    modifier = Modifier.clickable {
                        onSendMessageClicked(messageText)
                        messageText = ""
                    }
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
fun MessageList(messages: List<Message>, currentProfileId: String) {
    LazyColumn(
        modifier = Modifier
            .wrapContentSize()
            .padding(16.dp)
    ) {
        items(messages) { message ->
            MessageItem(message = message, currentProfileId = currentProfileId)

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun MessageItem(message: Message, currentProfileId: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = if ("${message.from}".lowercase()
                .contains(currentProfileId.lowercase())
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
                text = message.content ?: "",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            if (message.createdAt != null) {
                Text(
                    text = message.createdAt.toHourMinuteString(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.align(alignment = Alignment.End)
                )
            }
        }
    }
}