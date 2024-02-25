package com.example.hiretop.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hiretop.data.datastore.HireTopDataStoreRepos
import com.example.hiretop.di.repository.ChatItemRepository
import com.example.hiretop.di.repository.MessageRepository
import com.example.hiretop.models.ChatItemUI
import com.example.hiretop.models.Message
import com.google.firebase.firestore.ListenerRegistration
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatItemRepository: ChatItemRepository,
    private val messageRepository: MessageRepository,
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

    private var snapshotListener: ListenerRegistration? = null

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
                    _chatItemsUI.update { it }
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

    fun sendMessage(input: String, onSuccess: (String) -> Unit, onFailure: (String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            // Create message instance
            val message = Message(
                subject = "", // Chat ID
                to = "", // Receiver ID
                from = "", // Sender ID
                content = input,
                received = false,
                isRead = false,
                createdAt = System.currentTimeMillis()
            )

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

    override fun onCleared() {
        super.onCleared()
        // Add the listener to the coroutine scope
        snapshotListener?.remove()
    }
}