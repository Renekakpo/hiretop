package com.example.hiretop.models

import com.example.hiretop.utils.Utils.convertLongToDate
import com.google.firebase.firestore.DocumentId
import java.util.Date

data class Message(
    @DocumentId
    val messageId: String? = null,
    val subject: String? = null, // Chat ID
    val to: String? = null, // Receiver ID
    val from: String? = null, // Sender ID
    val content: String? = null,
    val received: Boolean = false,
    val isRead: Boolean = false,
    val createdAt: Long? = null
) {
    constructor() : this(null, null, null, null, null, false, false, null)

    fun getCreatedDate(): Date? {
        return createdAt?.let { convertLongToDate(it) }
    }
}

// Mock data
val mockMessages = listOf(
    Message(
        subject = "sender_id_1",
        to = "sender_id_1",
        from = "receiver_id_1",
        content = "Hello, how are you?",
        createdAt = System.currentTimeMillis() - 3600000 // 1 hour ago
    ),
    Message(
        subject = "sender_id_1",
        to = "sender_id_1",
        from = "receiver_id_1",
        content = "I'm doing great, thank you!",
        createdAt = System.currentTimeMillis() - 7200000 // 2 hours ago
    ),
    Message(
        subject = "sender_id_1",
        to = "sender_id_1",
        from = "receiver_id_1",
        content = "Would you like to meet for coffee?",
        createdAt = System.currentTimeMillis() - 10800000 // 3 hours ago
    ),
    Message(
        subject = "sender_id_1",
        to = "sender_id_1",
        from = "receiver_id_1",
        content = "Sure, I'd love to!",
        createdAt = System.currentTimeMillis() - 14400000 // 4 hours ago
    )
)
