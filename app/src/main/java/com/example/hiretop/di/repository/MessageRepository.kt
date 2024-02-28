package com.example.hiretop.di.repository

import com.example.hiretop.R
import com.example.hiretop.app.HireTop.Companion.appContext
import com.example.hiretop.models.Message
import com.example.hiretop.utils.Constant.MESSAGES_COLLECTION_NAME
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ListenerRegistration
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

/**
 * Repository class for managing messages.
 * This class handles sending, marking as read, and fetching messages.
 */
@Singleton
class MessageRepository @Inject constructor(
    @Named(MESSAGES_COLLECTION_NAME)
    private val messagesCollection: CollectionReference,
) {

    /**
     * Function to send a message.
     * @param message The message to be sent.
     * @param onSuccess Callback function invoked upon successful message sending, providing the message ID.
     * @param onFailure Callback function invoked upon failure, providing an error message.
     */
    fun sendMessage(
        message: Message,
        onSuccess: (String) -> Unit,
        onFailure: (String) -> Unit
    ) {
        // Add message to the messages collection
        messagesCollection
            .add(message)
            .addOnSuccessListener {
                // Invoke onSuccess callback with the message ID
                onSuccess(it.id)
            }
            .addOnFailureListener {
                // Invoke onFailure callback with an error message, fallback to a default string resource if message is null
                onFailure(it.message ?: appContext.getString(R.string.send_message_failure_text))
            }
    }

    /**
     * Function to mark a message as read.
     * @param messageID The ID of the message to mark as read.
     * @param onSuccess Callback function invoked upon successful marking as read.
     * @param onFailure Callback function invoked upon failure, providing an error message.
     */
    fun markMessageAsRead(
        messageID: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        // Update the 'isRead' field of the message document to true
        messagesCollection
            .document(messageID)
            .update("isRead", true)
            .addOnSuccessListener {
                // Invoke onSuccess callback
                onSuccess()
            }
            .addOnFailureListener {
                // Invoke onFailure callback with an error message, fallback to a default string resource if message is null
                onFailure(it.message ?: appContext.getString(R.string.fetch_messages_failure_text))
            }
    }

    /**
     * Function to fetch messages for a specific user.
     * @param chatId The ID of the chat for which messages are fetched.
     * @param onSuccess Callback function invoked upon successful fetching of messages, providing the list of messages.
     * @param onFailure Callback function invoked upon failure, providing an error message.
     */
    fun fetchMessages(
        chatId: String,
        onSuccess: (List<Message>) -> Unit,
        onFailure: (String) -> Unit
    ) {
        // Query messages collection for messages with the specified chat ID
        messagesCollection
            .whereEqualTo("subject", chatId)
            .get()
            .addOnSuccessListener { snapshot ->
                val messages = mutableListOf<Message>()

                // If snapshot is not empty, convert documents to Message objects and add to messages list
                if (!snapshot.isEmpty) {
                    snapshot.forEach { item ->
                        val message = item.toObject(Message::class.java)
                        messages.add(message)
                    }
                }

                // Sort messages by createdAt property in ascending order (from old to new)
                val sortedMessages = messages.sortedBy { it.createdAt }
                // Invoke onSuccess callback with the sorted list of messages
                onSuccess(sortedMessages)
            }
            .addOnFailureListener {
                // Invoke onFailure callback with an error message, fallback to a default string resource if message is null
                onFailure(it.message ?: appContext.getString(R.string.fetch_messages_failure_text))
            }
    }

    /**
     * Function to listen for new messages.
     * @param chatId The ID of the chat to listen for new messages.
     * @param onException Callback function invoked upon exception, providing an error message.
     * @param onUpdate Callback function invoked upon receiving new messages, providing the updated list of messages.
     * @return ListenerRegistration object for managing the snapshot listener.
     */
    fun getNewMessagesSnapshotListener(
        chatId: String,
        onException: (String) -> Unit,
        onUpdate: (List<Message>) -> Unit
    ): ListenerRegistration {
        // Create a reference to the messages collection for the specified chatId
        val messagesRef = messagesCollection.whereEqualTo("subject", chatId)

        // Create and return the snapshot listener
        return messagesRef.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                // Invoke onException callback with an error message, fallback to a default string resource if message is null
                onException(exception.message ?: appContext.getString(R.string.unknown_error_text))
                return@addSnapshotListener
            }

            // If the snapshot is not null and it's not empty
            snapshot?.let { snap ->
                val messages = snap.documents.mapNotNull { doc ->
                    doc.toObject(Message::class.java)
                }
                // Invoke onUpdate callback with the list of messages
                onUpdate(messages)
            }
        }
    }
}