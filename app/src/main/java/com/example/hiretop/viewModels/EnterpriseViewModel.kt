package com.example.hiretop.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hiretop.R
import com.example.hiretop.app.HireTop.Companion.appContext
import com.example.hiretop.data.datastore.HireTopDataStoreRepos
import com.example.hiretop.models.EnterpriseProfile
import com.example.hiretop.models.JobOffer
import com.example.hiretop.utils.Constant.ENTERPRISES_COLLECTION_NAME
import com.example.hiretop.utils.Constant.JOB_OFFERS_COLLECTION_NAME
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class EnterpriseViewModel @Inject constructor(
    private val db: FirebaseFirestore,
    @Named(JOB_OFFERS_COLLECTION_NAME)
    private val jobOffersCollection: CollectionReference,
    @Named(ENTERPRISES_COLLECTION_NAME)
    private val enterprisesCollection: CollectionReference,
    private val appDataStore: HireTopDataStoreRepos
) : ViewModel() {

    private val _jobOffersList = MutableStateFlow<List<JobOffer>?>(null)
    val jobOffersList: StateFlow<List<JobOffer>?> = _jobOffersList

    private val _mJobOffer = MutableStateFlow<JobOffer?>(null)
    val mJobOffer: StateFlow<JobOffer?> = _mJobOffer

    private val _enterpriseProfile = MutableStateFlow<EnterpriseProfile?>(null)
    val enterpriseProfile: StateFlow<EnterpriseProfile?> = _enterpriseProfile

    fun addJobOffer(jobOffer: JobOffer, onSuccess: (String) -> Unit, onFailure: (String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            jobOffersCollection.add(jobOffer)
                .addOnSuccessListener { doc ->
                    _mJobOffer.update {
                        it?.copy(jobOfferID = doc.id)
                    }
                    onSuccess(doc.id)
                }
                .addOnFailureListener {
                    onFailure(
                        it.message ?: appContext.getString(R.string.add_job_offer_failure_text)
                    )
                }
        }
    }

    fun updateJobOffer(jobOffer: JobOffer, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            jobOffer.jobOfferID?.let { jobOfferID ->
                jobOffersCollection.document(jobOfferID).set(jobOffer)
                    .addOnSuccessListener {
                        onSuccess()
                    }
                    .addOnFailureListener {
                        onFailure(
                            it.message
                                ?: appContext.getString(R.string.update_job_offer_failure_text)
                        )
                    }
            }
        }
    }

    fun deleteJobOffer(jobOfferID: String) {
        viewModelScope.launch(Dispatchers.IO) {
            jobOffersCollection.document(jobOfferID).delete()
                .addOnSuccessListener {
                    TODO("Handle success")
                }
                .addOnFailureListener {
                    TODO("Handle failure")
                }
        }
    }

    fun getAllJobOffersForEnterprise(enterpriseID: String, onFailure: (String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            jobOffersCollection
                .whereEqualTo("enterpriseID", enterpriseID)
                .get()
                .addOnSuccessListener { snapshot ->
                    val offersList = mutableListOf<JobOffer>()
                    snapshot.forEach { document ->
                        val jobOffer = document.toObject(JobOffer::class.java)
                        offersList.add(jobOffer)
                    }
                    _jobOffersList.value = offersList
                }
                .addOnFailureListener {
                    onFailure(
                        it.message ?: appContext.getString(R.string.read_job_offers_failure_text)
                    )
                }
        }
    }

    /**
     * Function to get auto-generated ID by Firestore when creating a new profile
     */
    fun createNewEnterpriseProfile(
        profile: EnterpriseProfile,
        onSuccess: (String) -> Unit,
        onFailure: (String) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            enterprisesCollection
                .add(profile)
                .addOnSuccessListener { documentReference ->
                    saveEnterpriseProfileId(profileId = documentReference.id)
                    onSuccess(documentReference.id)
                }
                .addOnFailureListener {
                    onFailure(
                        it.message ?: it.localizedMessage
                        ?: appContext.getString(R.string.create_enterprise_profile_failure_text)
                    )
                }
        }
    }

    private fun saveEnterpriseProfileId(profileId: String) {
        viewModelScope.launch {
            appDataStore.saveEnterpriseProfileIdState(profileId)
        }
    }

    fun addOrUpdateEnterpriseProfile(enterpriseId: String, profile: EnterpriseProfile) {
        viewModelScope.launch(Dispatchers.IO) {
            val enterpriseRef = enterprisesCollection.document(enterpriseId)
            if (profile.enterpriseID == null) {
                enterpriseRef.set(profile)
                    .addOnSuccessListener {
                        TODO("Handle success")
                    }
                    .addOnFailureListener {
                        TODO("Handle failure")
                    }
            } else {
                enterpriseRef.set(profile.copy(enterpriseID = null))
                    .addOnSuccessListener {
                        TODO("Handle success")
                    }
                    .addOnFailureListener {
                        TODO("Handle failure")
                    }
            }
        }
    }

    fun getEnterpriseProfile(enterpriseId: String, callback: (EnterpriseProfile?) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            enterprisesCollection.document(enterpriseId)
                .get()
                .addOnSuccessListener { document ->
                    val profile = document.toObject<EnterpriseProfile>()
                    callback(profile)
                }
                .addOnFailureListener {
                    callback(null)
                }
        }
    }

    fun addOrEditBannerImage(enterpriseId: String, bannerUrl: String, callback: (String?) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            enterprisesCollection.document(enterpriseId)
                .update("bannerUrl", bannerUrl)
                .addOnSuccessListener {
                    callback("success")
                }
                .addOnFailureListener {
                    callback(it.message ?: "no error message")
                }
        }
    }

    fun addOrEditProfilePicture(
        enterpriseId: String,
        pictureUrl: String,
        callback: (String?) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            enterprisesCollection.document(enterpriseId)
                .update("pictureUrl", pictureUrl)
                .addOnSuccessListener {
                    callback("success")
                }
                .addOnFailureListener {
                    callback(it.message ?: it.localizedMessage ?: "no error message")
                }
        }
    }
}