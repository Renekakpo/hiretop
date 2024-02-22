package com.example.hiretop.di.repository

import com.example.hiretop.models.Message
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import javax.inject.Inject

class MessageRepository @Inject constructor(
    private val db: FirebaseFirestore,
    private val messagesCollection: CollectionReference,
) {

    /**
     * Function to send a message
     */
    suspend fun sendMessage(message: Message) {
        messagesCollection
            .add(message)
            .addOnSuccessListener {
                TODO("Handle success")
            }
            .addOnFailureListener {
                TODO("Handle failure")
            }
    }

    /**
     * Function to mark a message as received
     */
    suspend fun markMessageAsReceived(messageID: String) {
        messagesCollection
            .document(messageID)
            .update("received", true)
            .addOnSuccessListener {
                TODO("Handle success")
            }
            .addOnFailureListener {
                TODO("Handle failure")
            }
    }

    /**
     * Function to mark a message as read
     */
    suspend fun markMessageAsRead(messageID: String) {
        messagesCollection
            .document(messageID)
            .update("isRead", true)
            .addOnSuccessListener {
                TODO("Handle success")
            }
            .addOnFailureListener {
                TODO("Handle failure")
            }
    }

    /**
     * Function to fetch messages for a specific user
     */
    suspend fun fetchMessages(chatId: String) {
        messagesCollection
            .whereEqualTo("subject", chatId)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener {
                TODO("Handle success")
            }
            .addOnFailureListener {
                TODO("Handle failure")
            }
    }

    /**
     * Function to fetch unread messages for a specific user
     */
    suspend fun fetchUnreadMessages(userID: String) {
        messagesCollection
            .whereEqualTo("to", userID)
            .whereEqualTo("isRead", false)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener {
                TODO("Handle success")
            }
            .addOnFailureListener {
                TODO("Handle failure")
            }
    }

    /**
     * Function to fetch received messages for a specific user
     */
    suspend fun fetchReceivedMessages(chatId: String) {
        messagesCollection
            .whereEqualTo("subject", chatId)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener {
                TODO("Handle success")
            }
            .addOnFailureListener {
                TODO("Handle failure")
            }
    }
}