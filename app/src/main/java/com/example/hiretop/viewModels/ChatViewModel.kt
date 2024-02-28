package com.example.hiretop.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hiretop.R
import com.example.hiretop.app.HireTop.Companion.appContext
import com.example.hiretop.data.datastore.HireTopDataStoreRepos
import com.example.hiretop.di.repository.ChatItemRepository
import com.example.hiretop.di.repository.MessageRepository
import com.example.hiretop.models.ChatItem
import com.example.hiretop.models.ChatItemUI
import com.example.hiretop.models.JobApplication
import com.example.hiretop.models.Message
import com.example.hiretop.utils.Constant
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ListenerRegistration
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatItemRepository: ChatItemRepository,
    private val messageRepository: MessageRepository,
    @Named(Constant.JOB_APPLICATIONS_COLLECTION_NAME)
    private val jobApplicationsCollection: CollectionReference,
    appDataStore: HireTopDataStoreRepos,
) : ViewModel() {

    // Flow to hold the account type
    val isEnterpriseAccount: Flow<Boolean?> = appDataStore.isEnterpriseAccount

    // Flow to hold the candidate profile id
    val candidateProfileId: Flow<String?> = appDataStore.candidateProfileId

    // Flow to hold the enterprise profile id
    val enterpriseProfileId: Flow<String?> = appDataStore.enterpriseProfileId

    // Flow to hold chat item ui element
    private val _chatItemsUI = MutableStateFlow<List<ChatItemUI>?>(null)
    val chatItemsUI: StateFlow<List<ChatItemUI>?> = _chatItemsUI

    // Flow to hold messages
    private val _messages = MutableStateFlow<List<Message>?>(null)
    val messages: StateFlow<List<Message>?> = _messages

    // Flow to hold job application
    private val _jobApplication = MutableStateFlow<JobApplication?>(null)
    val jobApplication: StateFlow<JobApplication?> = _jobApplication

    private var snapshotListener: ListenerRegistration? = null

    fun createOrEditChatItem(
        chatItem: ChatItem,
        onSuccess: (String) -> Unit,
        onFailure: (String) -> Unit
    ) {
        viewModelScope.launch {
            chatItemRepository.createOrEditChatItem(
                chatItem = chatItem,
                onSuccess = {
                    onSuccess(it)
                },
                onFailure = { onFailure(it) }
            )
        }
    }

    fun getChatItemsUI(
        profileId: String,
        isEnterpriseAccount: Boolean,
        onSuccess: (List<ChatItemUI>) -> Unit,
        onFailure: (String) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            chatItemRepository.getChatItemUIList(
                profileId = profileId,
                isEnterpriseAccount = isEnterpriseAccount,
                onSuccess = { list ->
                    _chatItemsUI.value = list

                    Thread.sleep(300)
                    onSuccess(list)
                },
                onFailure = { onFailure(it) }
            )
        }
    }

    fun getListOfMessages(
        chatId: String,
        onSuccess: (List<Message>) -> Unit,
        onFailure: (String) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            messageRepository.fetchMessages(
                chatId = chatId,
                onSuccess = {
                    onSuccess(it)
                },
                onFailure = {
                    onFailure(it)
                }
            )
        }
    }

    fun sendMessage(message: Message, onSuccess: (String) -> Unit, onFailure: (String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            messageRepository.sendMessage(
                message = message,
                onSuccess = {
                    onSuccess(it)
                },
                onFailure = {
                    onFailure(it)
                }
            )
        }
    }

    fun observeNewMessageByChatId(
        chatId: String,
        onSuccess: (List<Message>) -> Unit,
        onFailure: (String) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Create a Firestore snapshot listener for new messages
                snapshotListener = messageRepository.getNewMessagesSnapshotListener(
                    chatId = chatId,
                    onException = { onFailure(it) }
                ) { messages ->
                    // Sort messages by createdAt in descending order
                    val sortedMessages = messages.sortedByDescending { it.createdAt }
                    // Update _messages
                    _messages.value = sortedMessages
                    // Invoke onSuccess with the sorted messages
                    onSuccess(sortedMessages)
                }
            } catch (e: Exception) {
                onFailure(e.message ?: "Unknown error occurred")
            }
        }
    }

    fun markMessageAsRead(messageId: String) {
        viewModelScope.launch {
            messageRepository.markMessageAsRead(
                messageID = messageId,
                onSuccess = {},
                onFailure = {}
            )
        }
    }

    fun getJobApplicationFromChatItemUI(jobApplicationId: String, onSuccess : () -> Unit, onFailure: (String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                jobApplicationsCollection.document(jobApplicationId)
                    .get()
                    .addOnSuccessListener { snapshot ->
                        val app = snapshot.toObject(JobApplication::class.java)
                        _jobApplication.value = app
                        onSuccess()
                    }
                    .addOnFailureListener {
                        onFailure(
                            it.localizedMessage ?: appContext.getString(R.string.unknown_error_text)
                        )
                    }
            } catch (e: Exception) {
                Log.d("getJobApplicationFromChatItemUI", "${e.message}")
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        // Add the listener to the coroutine scope
        snapshotListener?.remove()
    }
}