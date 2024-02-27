package com.example.hiretop.models

import com.google.firebase.firestore.DocumentId

data class ChatItemUI(
    val chatId: String? = null,
    val pictureUrl: String?, // Candidate or Enterprise picture url
    val profileName: String?, // Candidate or Enterprise name
    val offerTitle: String, // Job offer title
    val unreadMessageCount: Long = 0, // Unread message count
    val jobApplicationId: String
)

data class ChatItem(
    @DocumentId
    val chatId: String? = null,
    val candidateProfileId: String? = null,
    val enterpriseProfileId: String? = null,
    val jobOfferId: String? = null,
    val jobApplicationId: String? = null,
) {
    constructor() : this(null, null, null, null, null)
}
