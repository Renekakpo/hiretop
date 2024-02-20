package com.example.hiretop.models

data class Message(
    val sender: Map<String, String>,
    val receiver: Map<String, String>,
    val content: String,
    val received: Boolean = false,
    val isRead: Boolean = false,
    val createdAt: Long
)

// Mock data
val mockMessages = listOf(
    Message(
        sender = mapOf("id" to "sender_id_1", "name" to "John Doe"),
        receiver = mapOf("id" to "receiver_id_1", "name" to "Jane Smith"),
        content = "Hello, how are you?",
        createdAt = System.currentTimeMillis() - 3600000 // 1 hour ago
    ),
    Message(
        sender = mapOf("id" to "sender_id_2", "name" to "Alice Johnson"),
        receiver = mapOf("id" to "receiver_id_2", "name" to "Bob Brown"),
        content = "I'm doing great, thank you!",
        createdAt = System.currentTimeMillis() - 7200000 // 2 hours ago
    ),
    Message(
        sender = mapOf("id" to "sender_id_1", "name" to "John Doe"),
        receiver = mapOf("id" to "receiver_id_1", "name" to "Jane Smith"),
        content = "Would you like to meet for coffee?",
        createdAt = System.currentTimeMillis() - 10800000 // 3 hours ago
    ),
    Message(
        sender = mapOf("id" to "sender_id_2", "name" to "Alice Johnson"),
        receiver = mapOf("id" to "receiver_id_2", "name" to "Bob Brown"),
        content = "Sure, I'd love to!",
        createdAt = System.currentTimeMillis() - 14400000 // 4 hours ago
    )
)
