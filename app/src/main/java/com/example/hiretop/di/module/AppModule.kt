package com.example.hiretop.di.module

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.hiretop.data.datastore.HireTopDataStoreRepos
import com.example.hiretop.helpers.FirebaseHelper
import com.example.hiretop.helpers.PermissionsHelper
import com.example.hiretop.utils.Constant
import com.example.hiretop.utils.Constant.CANDIDATES_COLLECTION_NAME
import com.example.hiretop.utils.Constant.CHATS_COLLECTION_NAME
import com.example.hiretop.utils.Constant.ENTERPRISES_COLLECTION_NAME
import com.example.hiretop.utils.Constant.JOB_APPLICATIONS_COLLECTION_NAME
import com.example.hiretop.utils.Constant.JOB_OFFERS_COLLECTION_NAME
import com.example.hiretop.utils.Constant.MESSAGES_COLLECTION_NAME
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
       return  context.dataStore
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
     * Annotation to tell Hilt this method provides an instance of FirebaseHelper
     */
    @Provides
    @Singleton
    fun provideFirebaseHelper(
        db: FirebaseFirestore,
        @Named(JOB_OFFERS_COLLECTION_NAME) jobOffersCollection: CollectionReference
    ): FirebaseHelper {
        return FirebaseHelper(db, jobOffersCollection)
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
        return db.collection(Constant.JOB_APPLICATIONS_COLLECTION_NAME)
    }

    /**
     * Method to provide CollectionReference for job offers.
     */
    @Singleton
    @Provides
    @Named(JOB_OFFERS_COLLECTION_NAME)
    fun provideJobOffersCollection(db: FirebaseFirestore): CollectionReference {
        return db.collection(Constant.JOB_OFFERS_COLLECTION_NAME)
    }

    /**
     * Method to provide CollectionReference for candidate profiles.
     */
    @Singleton
    @Provides
    @Named(CANDIDATES_COLLECTION_NAME)
    fun provideCandidateProfilesCollection(db: FirebaseFirestore): CollectionReference {
        return db.collection(Constant.CANDIDATES_COLLECTION_NAME)
    }

    /**
     * Method to provide CollectionReference for enterprise profiles.
     */
    @Singleton
    @Provides
    @Named(ENTERPRISES_COLLECTION_NAME)
    fun provideEnterpriseProfilesCollection(db: FirebaseFirestore): CollectionReference {
        return db.collection(Constant.ENTERPRISES_COLLECTION_NAME)
    }

    /**
     * Method to provide CollectionReference for chats.
     */
    @Singleton
    @Provides
    @Named(CHATS_COLLECTION_NAME)
    fun provideChatsCollection(db: FirebaseFirestore): CollectionReference {
        return db.collection(Constant.CHATS_COLLECTION_NAME)
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
    fun providePermissionsHelper(context: Context): PermissionsHelper {
        return PermissionsHelper(context)
    }

}