package com.example.hiretop.di.repository

import com.example.hiretop.R
import com.example.hiretop.app.HireTop.Companion.appContext
import com.example.hiretop.models.CandidateProfile
import com.example.hiretop.models.ChatItem
import com.example.hiretop.models.ChatItemUI
import com.example.hiretop.models.EnterpriseProfile
import com.example.hiretop.models.JobOffer
import com.example.hiretop.utils.Constant.CANDIDATES_COLLECTION_NAME
import com.example.hiretop.utils.Constant.CHATS_COLLECTION_NAME
import com.example.hiretop.utils.Constant.ENTERPRISES_COLLECTION_NAME
import com.example.hiretop.utils.Constant.JOB_OFFERS_COLLECTION_NAME
import com.example.hiretop.utils.Constant.MESSAGES_COLLECTION_NAME
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

// Repository for managing chat items
@Singleton
class ChatItemRepository @Inject constructor(
    @Named(MESSAGES_COLLECTION_NAME)
    private val messagesCollection: CollectionReference, // Collection reference for messages
    @Named(CHATS_COLLECTION_NAME)
    private val chatsCollection: CollectionReference, // Collection reference for chats
    @Named(CANDIDATES_COLLECTION_NAME)
    private val candidateProfilesCollection: CollectionReference, // Collection reference for candidate profiles
    @Named(ENTERPRISES_COLLECTION_NAME)
    private val enterpriseProfilesCollection: CollectionReference, // Collection reference for enterprise profiles
    @Named(JOB_OFFERS_COLLECTION_NAME)
    private val jobOffersCollection: CollectionReference // Collection reference for job offers
) {

    /**
     * Get the list of chat items
     */
    suspend fun getChatItemUIList(
        profileId: String,
        isEnterpriseAccount: Boolean,
        onSuccess: (List<ChatItemUI>) -> Unit,
        onFailure: (String) -> Unit
    ) {
        try {
            // Determine the field to query based on the account type
            val field = if (isEnterpriseAccount) {
                "enterpriseProfileId"
            } else {
                "candidateProfileId"
            }

            // Query Firestore collection to get ChatItem documents
            val chatItemDocs = chatsCollection
                .whereEqualTo(field, profileId)
                .get()
                .await()

            // List to hold ChatItemUI objects
            val chatItemUIList = mutableListOf<ChatItemUI>()

            if (chatItemDocs.isEmpty) {
                onSuccess(emptyList())
            } else {
                // Iterate through each ChatItem document
                for (chatItemDoc in chatItemDocs) {
                    val chatItem = chatItemDoc.toObject(ChatItem::class.java)

                    // Fetch job offer
                    val jobOfferDoc =
                        chatItem.jobOfferId?.let { jobOffersCollection.document(it).get().await() }
                    val jobOffer = jobOfferDoc?.toObject(JobOffer::class.java)

                    // Query Firestore collection to get Message documents related to this chat
                    val messageQuery = messagesCollection
                        .whereEqualTo("subject", chatItem.chatId)
                        .whereEqualTo("isRead", false)
                    val unreadMessageCount = messageQuery.get().await().size().toLong()

                    var chatItemUI: ChatItemUI

                    if (isEnterpriseAccount) {
                        // Fetch candidate profile
                        val candidateProfileDoc =
                            chatItem.candidateProfileId?.let {
                                candidateProfilesCollection.document(it).get()
                                    .await()
                            }
                        val candidateProfile =
                            candidateProfileDoc?.toObject(CandidateProfile::class.java)

                        // Create ChatItemUI object and add it to the list
                        chatItemUI = ChatItemUI(
                            chatId = chatItem.chatId ?: "",
                            pictureUrl = candidateProfile?.pictureUrl ?: "",
                            profileName = candidateProfile?.name ?: "",
                            offerTitle = jobOffer?.title ?: "",
                            unreadMessageCount = unreadMessageCount,
                            jobApplicationId = "${chatItem.jobApplicationId}"
                        )
                    } else {
                        // Fetch enterprise profile
                        val enterpriseProfileDoc =
                            chatItem.enterpriseProfileId?.let {
                                enterpriseProfilesCollection.document(it).get().await()
                            }
                        val enterpriseProfile =
                            enterpriseProfileDoc?.toObject(EnterpriseProfile::class.java)

                        // Create ChatItemUI object and add it to the list
                        chatItemUI = ChatItemUI(
                            chatId = chatItem.chatId ?: "",
                            pictureUrl = enterpriseProfile?.pictureUrl ?: "",
                            profileName = enterpriseProfile?.name ?: "",
                            offerTitle = jobOffer?.title ?: "",
                            unreadMessageCount = unreadMessageCount,
                            jobApplicationId = "${chatItem.jobApplicationId}"
                        )
                    }

                    chatItemUIList.add(chatItemUI)
                }

                onSuccess(chatItemUIList)
            }
        } catch (e: Exception) {
            onFailure(e.message ?: appContext.getString(R.string.chat_list_creation_failure_text))
        }
    }

    /**
     * Create or edit chat Item
     */
    fun createOrEditChatItem(
        chatItem: ChatItem,
        onSuccess: (String) -> Unit,
        onFailure: (String) -> Unit
    ) {
        if (chatItem.chatId.isNullOrEmpty()) {
            // Create a new chat item in the Firestore collection
            chatsCollection.add(chatItem)
                .addOnSuccessListener {
                    onSuccess(it.id)
                }
                .addOnFailureListener {
                    onFailure(it.message ?: appContext.getString(R.string.create_chat_failure_info))
                }
        } else {
            // Edit an existing chat item in the Firestore collection
            chatsCollection
                .document(chatItem.chatId)
                .set(chatItem, SetOptions.merge())
                .addOnSuccessListener {
                    onSuccess(chatItem.chatId)
                }
                .addOnFailureListener {
                    onFailure(
                        it.message ?: appContext.getString(R.string.edit_chat_item_failure_info)
                    )
                }
        }
    }

    /**
     * Check if a chat exists for a given application ID
     */
    fun chatExists(
        applicationId: String,
        onSuccess: (String) -> Unit,
        onFailure: (String) -> Unit
    ) {
        chatsCollection.whereEqualTo("jobApplicationId", applicationId)
            .get()
            .addOnSuccessListener { snapshot ->
                if (snapshot.isEmpty) {
                    onSuccess("")
                } else {
                    val chatItems = mutableListOf<ChatItem>()
                    snapshot.forEach {
                        val obj = it.toObject(ChatItem::class.java)
                        chatItems.add(obj)
                    }
                    onSuccess(chatItems.first().chatId ?: "")
                }
            }
            .addOnFailureListener { onFailure(it.localizedMessage ?: "") }
    }
}