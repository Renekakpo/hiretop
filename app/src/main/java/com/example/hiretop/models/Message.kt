package com.example.hiretop.models

import com.google.firebase.firestore.DocumentId

data class Message(
    @DocumentId
    val messageId: String? = null,
    val subject: String, // Chat ID
    val to: String, // Receiver ID
    val from: String, // Sender ID
    val content: String,
    val received: Boolean = false,
    val isRead: Boolean = false,
    val createdAt: Long
)

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
