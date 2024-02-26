package com.example.hiretop.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hiretop.R
import com.example.hiretop.app.HireTop.Companion.appContext
import com.example.hiretop.data.datastore.HireTopDataStoreRepos
import com.example.hiretop.di.repository.JobOfferApplicationRepository
import com.example.hiretop.helpers.FirebaseHelper
import com.example.hiretop.models.CandidateProfile
import com.example.hiretop.models.EnterpriseProfile
import com.example.hiretop.models.JobApplication
import com.example.hiretop.models.JobOffer
import com.example.hiretop.utils.Constant.ENTERPRISES_COLLECTION_NAME
import com.example.hiretop.utils.Constant.JOB_APPLICATIONS_COLLECTION_NAME
import com.example.hiretop.utils.Constant.JOB_OFFERS_COLLECTION_NAME
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.toObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.InputStream
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class EnterpriseViewModel @Inject constructor(
    private val db: FirebaseFirestore,
    @Named(JOB_OFFERS_COLLECTION_NAME)
    private val jobOffersCollection: CollectionReference,
    @Named(ENTERPRISES_COLLECTION_NAME)
    private val enterprisesCollection: CollectionReference,
    @Named(JOB_APPLICATIONS_COLLECTION_NAME)
    private val jobApplicationsCollection: CollectionReference,
    private val appDataStore: HireTopDataStoreRepos,
    private val firebaseHelper: FirebaseHelper,
    private val jobOfferApplicationRepository: JobOfferApplicationRepository,
) : ViewModel() {

    // Flow to hold the enterprise profile id
    val enterpriseProfileId: Flow<String?> = appDataStore.enterpriseProfileId

    private val _jobOffersList = MutableStateFlow<List<JobOffer>?>(null)
    val jobOffersList: StateFlow<List<JobOffer>?> = _jobOffersList

    private val _mJobOffer = MutableStateFlow<JobOffer?>(null)
    val mJobOffer: StateFlow<JobOffer?> = _mJobOffer

    private val _enterpriseProfile = MutableStateFlow<EnterpriseProfile?>(null)
    val enterpriseProfile: StateFlow<EnterpriseProfile?> = _enterpriseProfile

    private val _retention = MutableStateFlow<Double>(0.0)
    val retention: StateFlow<Double> = _retention

    private val _conversion = MutableStateFlow<Double>(0.0)
    val conversion: StateFlow<Double> = _conversion

    private val _productivity = MutableStateFlow<Double>(0.0)
    val productivity: StateFlow<Double> = _productivity

    private val _views = MutableStateFlow<Long>(0)
    val views: StateFlow<Long> = _views

    private val _applications = MutableStateFlow<Long>(0)
    val applications: StateFlow<Long> = _applications

    private val _hired = MutableStateFlow<Long>(0)
    val hired: StateFlow<Long> = _hired

    private val _interviews = MutableStateFlow<Long>(0)
    val interviews: StateFlow<Long> = _interviews

    private val _jobApplications = MutableStateFlow<List<JobApplication>?>(null)
    val jobApplications: StateFlow<List<JobApplication>?> = _jobApplications

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

    fun deleteJobOffer(jobOfferID: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
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

    fun getAllJobOffersForEnterprise(
        enterpriseID: String,
        onSuccess: (List<JobOffer>) -> Unit,
        onFailure: (String) -> Unit
    ) {
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

                    onSuccess(offersList)
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
                    _enterpriseProfile.value = profile.copy(enterpriseID = documentReference.id)
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

    fun updateEnterpriseProfile(
        enterpriseId: String,
        updatedProfile: EnterpriseProfile,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            enterprisesCollection
                .document(enterpriseId)
                .set(updatedProfile, SetOptions.merge())
                .addOnSuccessListener {
                    _enterpriseProfile.value = updatedProfile
                    onSuccess()
                }
                .addOnFailureListener {
                    onFailure(
                        it.message
                            ?: appContext.getString(R.string.profile_update_failed_error_text)
                    )
                }
        }
    }

    fun getEnterpriseProfile(
        enterpriseId: String,
        onSuccess: (EnterpriseProfile) -> Unit,
        onFailure: (String) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            enterprisesCollection
                .document(enterpriseId)
                .get()
                .addOnSuccessListener { document ->
                    val profile = document.toObject(EnterpriseProfile::class.java)
                    if (profile == null) {
                        onFailure(appContext.getString(R.string.profile_not_found_text))
                    } else {
                        _enterpriseProfile.value = profile
                        onSuccess(profile)
                    }
                }
                .addOnFailureListener {
                    onFailure(
                        it.message ?: appContext.getString(R.string.read_profile_failure_text)
                    )
                }
        }
    }

    fun uploadFileToFirebaseStorageAndGetUrl(
        inputStream: InputStream,
        fileName: String,
        onSuccess: (String) -> Unit,
        onFailure: (String) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            firebaseHelper.uploadFileToFirebaseStorage(
                inputStream, fileName, onSuccess, onFailure
            )
        }

    }

    fun addOrEditBannerImage(
        enterpriseId: String,
        bannerUrl: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            enterprisesCollection.document(enterpriseId)
                .update("bannerUrl", bannerUrl)
                .addOnSuccessListener {
                    onSuccess()
                }
                .addOnFailureListener {
                    onFailure(it.message ?: "Échec de modification de la bannière.")
                }
        }
    }

    fun addOrEditProfilePicture(
        enterpriseId: String,
        pictureUrl: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            enterprisesCollection.document(enterpriseId)
                .update("pictureUrl", pictureUrl)
                .addOnSuccessListener {
                    onSuccess()
                }
                .addOnFailureListener {
                    onFailure(
                        it.message ?: it.localizedMessage
                        ?: "Échec de modification de la photo de profil."
                    )
                }
        }
    }

    fun calculateRetention(
        enterpriseId: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Count the number of active applications
                val activeApplicationsSnapshot = jobApplicationsCollection
                    .whereEqualTo("enterpriseProfileId", enterpriseId)
                    .whereEqualTo("withdrawn", false)
                    .get().await()
                val activeApplicationsCount = activeApplicationsSnapshot.size()

                // Count the total number of applications
                val totalApplicationsSnapshot = jobApplicationsCollection
                    .whereEqualTo("enterpriseProfileId", enterpriseId)
                    .get().await()
                val totalApplicationsCount = totalApplicationsSnapshot.size()

                // Calculate retention
                _retention.value = if (totalApplicationsCount > 0) {
                    (activeApplicationsCount.toDouble() / totalApplicationsCount.toDouble()) * 100
                } else {
                    0.0
                }

                onSuccess()
            } catch (e: Exception) {
                onFailure(e.message ?: appContext.getString(R.string.unkown_error_text))
            }
        }
    }

    fun calculateConversion(
        enterpriseId: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Count the number of applications with offer received
                val offerReceivedApplicationsSnapshot = jobApplicationsCollection
                    .whereEqualTo("enterpriseProfileId", enterpriseId)
                    .whereEqualTo("offerReceived", true)
                    .get().await()
                val offerReceivedApplicationsCount = offerReceivedApplicationsSnapshot.size()

                // Count the total number of applications
                val totalApplicationsSnapshot = jobApplicationsCollection
                    .whereEqualTo("enterpriseProfileId", enterpriseId)
                    .get().await()
                val totalApplicationsCount = totalApplicationsSnapshot.size()

                // Calculate conversion
                _conversion.value = if (totalApplicationsCount > 0) {
                    (offerReceivedApplicationsCount.toDouble() / totalApplicationsCount.toDouble()) * 100
                } else {
                    0.0
                }

                onSuccess()
            } catch (e: Exception) {
                onFailure(e.message ?: appContext.getString(R.string.unkown_error_text))
            }
        }
    }

    suspend fun calculateProductivity(
        enterpriseId: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ): Double {
        // Fetch all job offers for the enterprise
        val offersSnapshot = jobOffersCollection
            .whereEqualTo("enterpriseID", enterpriseId)
            .get().await()

        // Count the number of hires
        val totalHiresCount = offersSnapshot.sumBy { document ->
            document.toObject<JobOffer>().hireCount.toInt()
        }

        // Calculate productivity
        return totalHiresCount.toDouble()
    }

    fun calculateViews(
        enterpriseId: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                // Fetch all job offers for the enterprise
                val offersSnapshot = jobOffersCollection
                    .whereEqualTo("enterpriseID", enterpriseId)
                    .get().await()

                // Sum the view counts of all job offers
                _views.value = offersSnapshot.sumOf { document ->
                    document.toObject<JobOffer>().viewCount
                }

                onSuccess()
            } catch (e: Exception) {
                onFailure(e.message ?: appContext.getString(R.string.unkown_error_text))
            }
        }
    }

    fun calculateApplications(
        enterpriseId: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                // Count the total number of applications for the enterprise
                val applicationsSnapshot = jobApplicationsCollection
                    .whereEqualTo("enterpriseProfileId", enterpriseId)
                    .get().await()

                _applications.value = applicationsSnapshot.size().toLong()

                onSuccess()
            } catch (e: Exception) {
                onFailure(e.message ?: appContext.getString(R.string.unkown_error_text))
            }
        }
    }

    suspend fun calculateHires(
        enterpriseId: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                // Fetch all job offers for the enterprise
                val offersSnapshot = jobOffersCollection
                    .whereEqualTo("enterpriseID", enterpriseId)
                    .get().await()

                // Sum the hire counts of all job offers
                _hired.value = offersSnapshot.sumOf { document ->
                    document.toObject<JobOffer>().hireCount
                }

                onSuccess()
            } catch (e: Exception) {
                onFailure(e.message ?: appContext.getString(R.string.unkown_error_text))
            }
        }
    }

    fun calculateInterviews(
        enterpriseId: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                // Fetch all job applications for the enterprise
                val applicationsSnapshot = jobApplicationsCollection
                    .whereEqualTo("enterpriseProfileId", enterpriseId)
                    .get().await()

                // Count the number of applications with interview scheduled
                _interviews.value = applicationsSnapshot.count { document ->
                    document.toObject<JobApplication>().interviewDate != null
                }.toLong()

                onSuccess()
            } catch (e: Exception) {
                onFailure(e.message ?: appContext.getString(R.string.unkown_error_text))
            }
        }
    }

    fun getJobApplicationsList(
        enterpriseProfileId: String,
        companyName: String,
        onSuccess: (List<JobApplication>) -> Unit,
        onFailure: (String) -> Unit
    ) {
        viewModelScope.launch {
            jobOfferApplicationRepository.getJobOfferApplicationsForCompany(
                enterpriseProfileId = enterpriseProfileId,
                companyName = companyName,
                onSuccess = { applications ->
                    _jobApplications.value = applications
                    onSuccess(applications)
                },
                onFailure = {
                    onFailure(it)
                }
            )
        }
    }

    fun editJobApplication(
        jobApplication: JobApplication,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            jobApplicationsCollection
                .document("${jobApplication.jobApplicationId}")
                .set(jobApplication, SetOptions.merge())
                .addOnSuccessListener {
                    onSuccess()
                }
                .addOnFailureListener {
                    onFailure(
                        it.message
                            ?: appContext.getString(R.string.application_update_failure_info)
                    )
                }
        }
    }

}