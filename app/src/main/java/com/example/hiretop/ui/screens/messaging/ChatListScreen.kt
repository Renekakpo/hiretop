package com.example.hiretop.ui.screens.messaging

import android.util.Log
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Badge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.hiretop.R
import com.example.hiretop.models.ChatItemUI
import com.example.hiretop.models.UIState
import com.example.hiretop.ui.extras.FailurePopup
import com.example.hiretop.ui.extras.HireTopCircularProgressIndicator
import com.example.hiretop.viewModels.ChatViewModel
import com.google.gson.Gson
import java.net.URLEncoder

@Composable
fun ChatListScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    chatViewModel: ChatViewModel = hiltViewModel()
) {
    val candidateProfileId by chatViewModel.candidateProfileId.collectAsState(null)
    val enterpriseProfileId by chatViewModel.enterpriseProfileId.collectAsState(null)
    val isEnterpriseAccount by chatViewModel.isEnterpriseAccount.collectAsState(null)
    val chatItemsUI by chatViewModel.chatItemsUI.collectAsState()
    var uiState by remember { mutableStateOf(UIState.LOADING) }
    var onErrorMessage by remember { mutableStateOf<String?>(null) }

    if (!onErrorMessage.isNullOrEmpty()) {
        FailurePopup(
            errorMessage = "$onErrorMessage",
            onDismiss = { onErrorMessage = null }
        )
    }

    LaunchedEffect(chatViewModel) {
        if (isEnterpriseAccount == null) {
            uiState = UIState.LOADING
        } else {
            if (isEnterpriseAccount == true) {
                if (enterpriseProfileId == null) {
                    uiState = UIState.FAILURE
                } else {
                    chatViewModel.getChatItemsUI(
                        profileId = "$enterpriseProfileId",
                        isEnterpriseAccount = isEnterpriseAccount!!,
                        onSuccess = {
                            uiState = if (chatItemsUI.isNullOrEmpty()) {
                                UIState.FAILURE
                            } else {
                                UIState.SUCCESS
                            }
                        },
                        onFailure = {
                            onErrorMessage = it
                            uiState = UIState.FAILURE
                        }
                    )
                }
            } else {
                if (candidateProfileId == null) {
                    uiState = UIState.FAILURE
                } else {
                    chatViewModel.getChatItemsUI(
                        profileId = "$candidateProfileId",
                        isEnterpriseAccount = isEnterpriseAccount!!,
                        onSuccess = {
                            uiState = if (it.isEmpty()) {
                                UIState.FAILURE
                            } else {
                                UIState.SUCCESS
                            }
                        },
                        onFailure = {
                            onErrorMessage = it
                            uiState = UIState.FAILURE
                        }
                    )
                }
            }
        }
    }

    when (uiState) {
        UIState.LOADING -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                // Display loader while fetching data
                HireTopCircularProgressIndicator()
            }
        }

        UIState.FAILURE -> {
            val failureInfo = if (enterpriseProfileId == null) {
                stringResource(R.string.enterprise_empty_chat_list_text)
            } else {
                stringResource(R.string.candidate_empty_chat_list_text)
            }

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                // Display text prompting user info
                Text(
                    text = failureInfo,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(16.dp),
                    textAlign = TextAlign.Center
                )
            }
        }

        UIState.SUCCESS -> {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.background)
                    .padding(15.dp)
            ) {
                Text(
                    text = stringResource(R.string.discussions_text),
                    style = MaterialTheme.typography.headlineMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.wrapContentWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                if (!chatItemsUI.isNullOrEmpty()) {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(15.dp)) {
                        itemsIndexed(chatItemsUI!!) { _, chatItemUI ->
                            ChatItemRow(
                                chatItemUI = chatItemUI,
                                onChatItemClicked = {
                                    try {
                                        val encodedURL = URLEncoder.encode(it.pictureUrl, "UTF-8")
                                        val chatItemUIJson =
                                            Gson().toJson(it.copy(pictureUrl = encodedURL))
                                        navController.navigate(route = "${ChatScreen.route}/$chatItemUIJson")
                                    } catch (e: Exception) {
                                        Log.d("onChatItemClicked", "${e.message}")
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatItemRow(chatItemUI: ChatItemUI, onChatItemClicked: (ChatItemUI) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(horizontal = 5.dp, vertical = 5.dp)
            .clickable { onChatItemClicked(chatItemUI) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context = LocalContext.current)
                .data(chatItemUI.pictureUrl?.let { URLEncoder.encode(it, "UTF-8") })
                .crossfade(true)
                .build(),
            contentDescription = stringResource(R.string.chat_item_profile_image_desc),
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (chatItemUI.profileName.isNullOrEmpty())
                        stringResource(R.string.unknown_text)
                    else
                        chatItemUI.profileName,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onBackground,
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
                    text = chatItemUI.offerTitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.wrapContentWidth()
                )

                Spacer(modifier = Modifier.weight(1f))

                if (chatItemUI.unreadMessageCount > 0) {
                    Badge(
                        modifier = Modifier.size(30.dp),
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ) {
                        Text(
                            text = if (chatItemUI.unreadMessageCount > 99) "+99"
                            else "$chatItemUI.unreadMessageCount",
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.align(alignment = Alignment.CenterVertically)
                        )
                    }
                }
            }
        }
    }
}