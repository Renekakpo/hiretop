package com.example.hiretop.di.repository

import com.example.hiretop.R
import com.example.hiretop.app.HireTop.Companion.appContext
import com.example.hiretop.models.Message
import com.example.hiretop.utils.Constant.MESSAGES_COLLECTION_NAME
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class MessageRepository @Inject constructor(
    private val db: FirebaseFirestore,
    @Named(MESSAGES_COLLECTION_NAME)
    private val messagesCollection: CollectionReference,
) {

    /**
     * Function to send a message
     */
    suspend fun sendMessage(
        message: Message,
        onSuccess: (String) -> Unit,
        onFailure: (String) -> Unit
    ) {
        messagesCollection
            .add(message)
            .addOnSuccessListener {
                onSuccess(it.id)
            }
            .addOnFailureListener {
                onFailure(it.message ?: appContext.getString(R.string.send_message_failure_text))
            }
    }

    /**
     * Function to mark a message as received
     */
    suspend fun markMessageAsReceived(
        messageID: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        messagesCollection
            .document(messageID)
            .update("received", true)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener {
                onFailure(it.message ?: appContext.getString(R.string.fetch_messages_failure_text))
            }
    }

    /**
     * Function to mark a message as read
     */
    suspend fun markMessageAsRead(
        messageID: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        messagesCollection
            .document(messageID)
            .update("isRead", true)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener {
                onFailure(it.message ?: appContext.getString(R.string.fetch_messages_failure_text))
            }
    }

    /**
     * Function to fetch messages for a specific user
     */
    suspend fun fetchMessages(
        chatId: String,
        onSuccess: (List<Message>) -> Unit,
        onFailure: (String) -> Unit
    ) {
        messagesCollection
            .whereEqualTo("subject", chatId)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { snapshot ->
                val messages = mutableListOf<Message>()

                if (!snapshot.isEmpty) {
                    snapshot.forEach { item ->
                        val message = item.toObject(Message::class.java)
                        messages.add(message)
                    }
                }

                onSuccess(messages)
            }
            .addOnFailureListener {
                onFailure(it.message ?: appContext.getString(R.string.fetch_messages_failure_text))
            }
    }

    /**
     * Function to fetch unread messages for a specific user
     */
    suspend fun fetchUnreadMessages(
        userID: String,
        onSuccess: (List<Message>) -> Unit,
        onFailure: (String) -> Unit
    ) {
        messagesCollection
            .whereEqualTo("to", userID)
            .whereEqualTo("isRead", false)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { snapshot ->
                val unreadMessages = mutableListOf<Message>()

                if (!snapshot.isEmpty) {
                    snapshot.forEach { item ->
                        val message = item.toObject(Message::class.java)
                        unreadMessages.add(message)
                    }
                }

                onSuccess(unreadMessages)
            }
            .addOnFailureListener {
                onFailure(
                    it.message ?: appContext.getString(R.string.fetch_unread_messages_failure_text)
                )
            }
    }

    /**
     * Function to fetch received messages for a specific user
     */
    suspend fun fetchReceivedMessages(
        chatId: String,
        onSuccess: (List<Message>) -> Unit,
        onFailure: (String) -> Unit
    ) {
        messagesCollection
            .whereEqualTo("subject", chatId)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { snapshot ->
                val receivedMessages = mutableListOf<Message>()

                if (!snapshot.isEmpty) {
                    snapshot.forEach { item ->
                        val message = item.toObject(Message::class.java)
                        receivedMessages.add(message)
                    }
                }

                onSuccess(receivedMessages)
            }
            .addOnFailureListener {
                onFailure(
                    it.message
                        ?: appContext.getString(R.string.fetch_received_messages_failure_text)
                )
            }
    }

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
                // Handle any errors
                onException(exception.message ?: appContext.getString(R.string.unkown_error_text))
                return@addSnapshotListener
            }

            // If the snapshot is not null and it's not empty
            snapshot?.let { snap ->
                val messages = snap.documents.mapNotNull { doc ->
                    doc.toObject(Message::class.java)
                }
                // Invoke onUpdate with the list of messages
                onUpdate(messages)
            }
        }
    }
}