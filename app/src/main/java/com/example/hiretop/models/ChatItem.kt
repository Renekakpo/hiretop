package com.example.hiretop.models

import com.google.firebase.firestore.DocumentId

data class ChatItemUI(
    val chatId: String,
    val pictureUrl: String, // Candidate or Enterprise picture url
    val profileName: String, // Candidate or Enterprise name
    val offerTitle: String, // Job offer title
    val unreadMessageCount: Long, // Unread message count
)

data class ChatItem(
    @DocumentId
    val chatId: String? = null,
    val profileId: String,
    val enterpriseId: String,
    val jobOfferId: String,
    val jobApplicationId: String,
)
