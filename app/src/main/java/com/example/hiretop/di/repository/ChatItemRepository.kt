package com.example.hiretop.di.repository

import com.example.hiretop.R
import com.example.hiretop.app.HireTop.Companion.appContext
import com.example.hiretop.models.CandidateProfile
import com.example.hiretop.models.ChatItem
import com.example.hiretop.models.ChatItemUI
import com.example.hiretop.models.EnterpriseProfile
import com.example.hiretop.models.JobApplication
import com.example.hiretop.models.JobOffer
import com.example.hiretop.utils.Constant.CANDIDATES_COLLECTION_NAME
import com.example.hiretop.utils.Constant.CHATS_COLLECTION_NAME
import com.example.hiretop.utils.Constant.ENTERPRISES_COLLECTION_NAME
import com.example.hiretop.utils.Constant.JOB_APPLICATIONS_COLLECTION_NAME
import com.example.hiretop.utils.Constant.JOB_OFFERS_COLLECTION_NAME
import com.example.hiretop.utils.Constant.MESSAGES_COLLECTION_NAME
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class ChatItemRepository @Inject constructor(
    private val db: FirebaseFirestore,
    @Named(MESSAGES_COLLECTION_NAME)
    private val messagesCollection: CollectionReference,
    @Named(CHATS_COLLECTION_NAME)
    private val chatsCollection: CollectionReference,
    @Named(CANDIDATES_COLLECTION_NAME)
    private val candidateProfilesCollection: CollectionReference,
    @Named(ENTERPRISES_COLLECTION_NAME)
    private val enterpriseProfilesCollection: CollectionReference,
    @Named(JOB_OFFERS_COLLECTION_NAME)
    private val jobOffersCollection: CollectionReference,
    @Named(JOB_APPLICATIONS_COLLECTION_NAME)
    private val jobApplicationsCollection: CollectionReference,
) {

    suspend fun getChatItemUI(jobApplicationId: String, isEnterpriseAccount: Boolean): ChatItemUI? {
        val chatItemSnapshot = chatsCollection.document(jobApplicationId)
            .get()
            .await()

        val chatItem = chatItemSnapshot.toObject(ChatItem::class.java) ?: return null

        // Fetch candidate profile
        val candidateProfileDoc =
            chatItem.candidateProfileId?.let {
                candidateProfilesCollection.document(it).get().await()
            }
        val candidateProfile = candidateProfileDoc?.toObject(CandidateProfile::class.java)

        // Fetch enterprise profile
        val enterpriseProfileDoc =
            chatItem.enterpriseProfileId?.let {
                enterpriseProfilesCollection.document(it).get().await()
            }
        val enterpriseProfile = enterpriseProfileDoc?.toObject(EnterpriseProfile::class.java)

        // Fetch job offer
        val jobOfferDoc =
            chatItem.jobOfferId?.let { jobOffersCollection.document(it).get().await() }
        val jobOffer = jobOfferDoc?.toObject(JobOffer::class.java)

        // Fetch job application
        val jobApplicationDoc =
            chatItem.jobApplicationId?.let { jobApplicationsCollection.document(it).get().await() }
        val jobApplication = jobApplicationDoc?.toObject(JobApplication::class.java)

        // Query Firestore collection to get Message documents related to this chat
        val messageQuery = messagesCollection
            .whereEqualTo("subject", chatItem.chatId)
            .whereEqualTo("isRead", false)
        val unreadMessageCount = messageQuery.get().await().size().toLong()

        // Create ChatItemUI object and return it
        return ChatItemUI(
            chatId = chatItem.chatId ?: "",
            pictureUrl = if (!isEnterpriseAccount) candidateProfile?.pictureUrl
                ?: "" else enterpriseProfile?.pictureUrl ?: "",
            profileName = if (!isEnterpriseAccount) candidateProfile?.name
                ?: "" else enterpriseProfile?.name ?: "",
            offerTitle = jobOffer?.title ?: "",
            unreadMessageCount = unreadMessageCount,
            jobApplicationId = jobApplicationId
        )
    }

    suspend fun getChatItemUIList(
        profileId: String,
        isEnterpriseAccount: Boolean,
        onSuccess: (List<ChatItemUI>) -> Unit,
        onFailure: (String) -> Unit
    ) {
        try {
            // Init conditional field
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
                        // Fetch enterprise profile
                        val enterpriseProfileDoc =
                            chatItem.enterpriseProfileId?.let {
                                enterpriseProfilesCollection.document(it).get()
                                    .await()
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
                    } else {
                        // Fetch candidate profile
                        val candidateProfileDoc =
                            chatItem.candidateProfileId?.let {
                                candidateProfilesCollection.document(it).get().await()
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
                    }

                    // Fetch job application
//                val jobApplicationDoc =
//                    jobApplicationsCollection.document(chatItem.jobApplicationId).get().await()
//                val jobApplication = jobApplicationDoc.toObject(JobApplication::class.java)

                    chatItemUIList.add(chatItemUI)
                }

                onSuccess(chatItemUIList)
            }
        } catch (e: Exception) {
            onFailure(e.message ?: appContext.getString(R.string.chat_list_creation_failure_text))
        }
    }

    suspend fun createOrEditChatItem(
        chatItem: ChatItem,
        onSuccess: (String) -> Unit,
        onFailure: (String) -> Unit
    ) {
        if (chatItem.chatId.isNullOrEmpty()) {
            chatsCollection.add(chatItem)
                .addOnSuccessListener {
                    onSuccess(it.id)
                }
                .addOnFailureListener {
                    onFailure(it.message ?: appContext.getString(R.string.create_chat_failure_info))
                }
        } else {
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

    suspend fun chatExists(
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