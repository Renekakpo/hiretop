package com.example.hiretop.di.repository

import com.example.hiretop.models.ChatItem
import com.example.hiretop.models.ChatItemUI
import com.example.hiretop.models.EnterpriseProfile
import com.example.hiretop.models.JobApplication
import com.example.hiretop.models.JobOffer
import com.example.hiretop.models.CandidateProfile
import com.example.hiretop.utils.Constant
import com.example.hiretop.utils.Constant.CANDIDATES_COLLECTION_NAME
import com.example.hiretop.utils.Constant.CHATS_COLLECTION_NAME
import com.example.hiretop.utils.Constant.ENTERPRISES_COLLECTION_NAME
import com.example.hiretop.utils.Constant.JOB_APPLICATIONS_COLLECTION_NAME
import com.example.hiretop.utils.Constant.JOB_OFFERS_COLLECTION_NAME
import com.example.hiretop.utils.Constant.MESSAGES_COLLECTION_NAME
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
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
            candidateProfilesCollection.document(chatItem.profileId).get().await()
        val candidateProfile = candidateProfileDoc.toObject(CandidateProfile::class.java)

        // Fetch enterprise profile
        val enterpriseProfileDoc =
            enterpriseProfilesCollection.document(chatItem.enterpriseId).get().await()
        val enterpriseProfile = enterpriseProfileDoc.toObject(EnterpriseProfile::class.java)

        // Fetch job offer
        val jobOfferDoc = jobOffersCollection.document(chatItem.jobOfferId).get().await()
        val jobOffer = jobOfferDoc.toObject(JobOffer::class.java)

        // Fetch job application
        val jobApplicationDoc =
            jobApplicationsCollection.document(chatItem.jobApplicationId).get().await()
        val jobApplication = jobApplicationDoc.toObject(JobApplication::class.java)

        // Query Firestore collection to get Message documents related to this chat
        val messageQuery = messagesCollection
            .whereEqualTo("subject", chatItem.chatId)
            .whereEqualTo("isRead", false)
        val unreadMessageCount = messageQuery.get().await().size().toLong()

        // Create ChatItemUI object and return it
        return ChatItemUI(
            chatId = chatItem.chatId,
            pictureUrl = if (!isEnterpriseAccount) candidateProfile?.pictureUrl
                ?: "" else enterpriseProfile?.pictureUrl ?: "",
            profileName = if (!isEnterpriseAccount) candidateProfile?.name
                ?: "" else enterpriseProfile?.name ?: "",
            offerTitle = jobOffer?.title ?: "",
            unreadMessageCount = unreadMessageCount
        )
    }

    suspend fun getChatItemUIList(
        profileId: String,
        isEnterpriseAccount: Boolean
    ): List<ChatItemUI> {
        // Query Firestore collection to get ChatItem documents
        val chatItemDocs = chatsCollection.get().await()

        // List to hold ChatItemUI objects
        val chatItemUIList = mutableListOf<ChatItemUI>()

        // Iterate through each ChatItem document
        for (chatItemDoc in chatItemDocs) {
            val chatItem = chatItemDoc.toObject(ChatItem::class.java)

            // Fetch candidate profile
            val candidateProfileDoc =
                candidateProfilesCollection.document(chatItem.profileId).get().await()
            val candidateProfile = candidateProfileDoc.toObject(CandidateProfile::class.java)

            // Fetch enterprise profile
            val enterpriseProfileDoc =
                enterpriseProfilesCollection.document(chatItem.enterpriseId).get().await()
            val enterpriseProfile = enterpriseProfileDoc.toObject(EnterpriseProfile::class.java)

            // Fetch job offer
            val jobOfferDoc = jobOffersCollection.document(chatItem.jobOfferId).get().await()
            val jobOffer = jobOfferDoc.toObject(JobOffer::class.java)

            // Fetch job application
            val jobApplicationDoc =
                jobApplicationsCollection.document(chatItem.jobApplicationId).get().await()
            val jobApplication = jobApplicationDoc.toObject(JobApplication::class.java)

            // Query Firestore collection to get Message documents related to this chat
            val messageQuery = messagesCollection
                .whereEqualTo("subject", chatItem.chatId)
                .whereEqualTo("isRead", false)
            val unreadMessageCount = messageQuery.get().await().size().toLong()

            // Create ChatItemUI object and add it to the list
            val chatItemUI = ChatItemUI(
                chatId = chatItem.chatId,
                pictureUrl = if (!isEnterpriseAccount) candidateProfile?.pictureUrl
                    ?: "" else enterpriseProfile?.pictureUrl ?: "",
                profileName = if (!isEnterpriseAccount) candidateProfile?.name
                    ?: "" else enterpriseProfile?.name ?: "",
                offerTitle = jobOffer?.title ?: "",
                unreadMessageCount = unreadMessageCount
            )
            chatItemUIList.add(chatItemUI)
        }

        return chatItemUIList
    }
}