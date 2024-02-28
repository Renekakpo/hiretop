package com.example.hiretop.di.module

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.hiretop.data.datastore.HireTopDataStoreRepos
import com.example.hiretop.di.repository.ChatItemRepository
import com.example.hiretop.di.repository.JobOfferApplicationRepository
import com.example.hiretop.di.repository.MessageRepository
import com.example.hiretop.helpers.FirebaseHelper
import com.example.hiretop.helpers.NetworkHelper
import com.example.hiretop.helpers.PermissionsHelper
import com.example.hiretop.utils.Constant.CANDIDATES_COLLECTION_NAME
import com.example.hiretop.utils.Constant.CHATS_COLLECTION_NAME
import com.example.hiretop.utils.Constant.ENTERPRISES_COLLECTION_NAME
import com.example.hiretop.utils.Constant.JOB_APPLICATIONS_COLLECTION_NAME
import com.example.hiretop.utils.Constant.JOB_OFFERS_COLLECTION_NAME
import com.example.hiretop.utils.Constant.MESSAGES_COLLECTION_NAME
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

/**
 * Annotation to tell Hilt this is a Dagger module
 */
@Module
@InstallIn(SingletonComponent::class) // Scope of the provided dependencies
object AppModule {
    /**
     * Provides an instance of DataStore<Preferences> using the preferencesDataStore delegate.
     */
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "hiretop_prefs")

    /**
     * Method to provide DataStore<Preferences>
     */
    @Singleton
    @Provides
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.dataStore
    }

    /**
     * Annotation to tell Hilt this method provides an instance of FirebaseFirestore
     */
    @Singleton
    @Provides
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    /**
     * Annotation to tell Hilt this method provides an instance of FirebaseAuth
     */
    @Singleton
    @Provides
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    /**
     * Annotation to tell Hilt this method provides an instance of FirebaseHelper
     */
    @Provides
    @Singleton
    fun provideFirebaseHelper(): FirebaseHelper {
        return FirebaseHelper()
    }

    /**
     * Annotation to tell Hilt this method provides an instance of JobOfferApplicationRepository
     */
    @Provides
    @Singleton
    fun provideJobOfferApplicationRepository(
        db: FirebaseFirestore,
        @Named(JOB_APPLICATIONS_COLLECTION_NAME) jobApplicationsCollection: CollectionReference,
        @Named(CANDIDATES_COLLECTION_NAME) candidateProfilesCollection: CollectionReference,
    ): JobOfferApplicationRepository {
        return JobOfferApplicationRepository(db, jobApplicationsCollection, candidateProfilesCollection)
    }

    /**
     * Annotation to tell Hilt this method provides an instance of ChatItemRepository
     */
    @Provides
    @Singleton
    fun provideChatItemRepository(
        @Named(MESSAGES_COLLECTION_NAME) messagesCollection: CollectionReference,
        @Named(CHATS_COLLECTION_NAME) chatsCollection: CollectionReference,
        @Named(CANDIDATES_COLLECTION_NAME) candidateProfilesCollection: CollectionReference,
        @Named(ENTERPRISES_COLLECTION_NAME) enterpriseProfilesCollection: CollectionReference,
        @Named(JOB_OFFERS_COLLECTION_NAME) jobOffersCollection: CollectionReference
    ): ChatItemRepository {
        return ChatItemRepository(
            messagesCollection,
            chatsCollection,
            candidateProfilesCollection,
            enterpriseProfilesCollection,
            jobOffersCollection,
        )
    }

    /**
     * Annotation to tell Hilt this method provides an instance of MessageRepository
     */
    @Provides
    @Singleton
    fun provideMessageRepository(
        db: FirebaseFirestore,
        @Named(MESSAGES_COLLECTION_NAME) messagesCollection: CollectionReference,
    ): MessageRepository {
        return MessageRepository(db, messagesCollection)
    }

    /**
     * Method to provide CollectionReference for messages collection
     */
    @Singleton
    @Provides
    @Named(MESSAGES_COLLECTION_NAME)
    fun provideMessagesCollection(db: FirebaseFirestore): CollectionReference {
        return db.collection(MESSAGES_COLLECTION_NAME)
    }

    /**
     * Method to provide CollectionReference for applications.
     */
    @Singleton
    @Provides
    @Named(JOB_APPLICATIONS_COLLECTION_NAME)
    fun provideJobApplicationsCollection(db: FirebaseFirestore): CollectionReference {
        return db.collection(JOB_APPLICATIONS_COLLECTION_NAME)
    }

    /**
     * Method to provide CollectionReference for job offers.
     */
    @Singleton
    @Provides
    @Named(JOB_OFFERS_COLLECTION_NAME)
    fun provideJobOffersCollection(db: FirebaseFirestore): CollectionReference {
        return db.collection(JOB_OFFERS_COLLECTION_NAME)
    }

    /**
     * Method to provide CollectionReference for candidate profiles.
     */
    @Singleton
    @Provides
    @Named(CANDIDATES_COLLECTION_NAME)
    fun provideCandidateProfilesCollection(db: FirebaseFirestore): CollectionReference {
        return db.collection(CANDIDATES_COLLECTION_NAME)
    }

    /**
     * Method to provide CollectionReference for enterprise profiles.
     */
    @Singleton
    @Provides
    @Named(ENTERPRISES_COLLECTION_NAME)
    fun provideEnterpriseProfilesCollection(db: FirebaseFirestore): CollectionReference {
        return db.collection(ENTERPRISES_COLLECTION_NAME)
    }

    /**
     * Method to provide CollectionReference for chats.
     */
    @Singleton
    @Provides
    @Named(CHATS_COLLECTION_NAME)
    fun provideChatsCollection(db: FirebaseFirestore): CollectionReference {
        return db.collection(CHATS_COLLECTION_NAME)
    }

    /**
     * Method to provide HireTopDataStoreRepos.
     */
    @Singleton
    @Provides
    fun provideHireTopDataStoreRepos(dataStore: DataStore<Preferences>): HireTopDataStoreRepos {
        return HireTopDataStoreRepos(dataStore)
    }

    /**
     * Methode to provide PermissionsHelper for permissions request.
     */
    @Singleton
    @Provides
    fun providePermissionsHelper(@ApplicationContext context: Context): PermissionsHelper {
        return PermissionsHelper(context)
    }

    /**
     * Methode to provide NetworkHelper for network capability check.
     */
    @Singleton
    @Provides
    fun provideNetworkHelper(@ApplicationContext context: Context): NetworkHelper {
        return NetworkHelper(context)
    }

}