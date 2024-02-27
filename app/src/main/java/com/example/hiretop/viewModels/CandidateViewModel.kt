package com.example.hiretop.viewModels

import android.util.Log
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
import com.example.hiretop.utils.Constant.CANDIDATES_COLLECTION_NAME
import com.example.hiretop.utils.Constant.JOB_APPLICATIONS_COLLECTION_NAME
import com.example.hiretop.utils.Constant.JOB_OFFERS_COLLECTION_NAME
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.toObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.InputStream
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class CandidateViewModel @Inject constructor(
    private val db: FirebaseFirestore,
    @Named(JOB_OFFERS_COLLECTION_NAME)
    private val jobOffersCollection: CollectionReference,
    @Named(CANDIDATES_COLLECTION_NAME)
    private val candidateProfilesCollection: CollectionReference,
    @Named(JOB_APPLICATIONS_COLLECTION_NAME)
    private val jobApplicationsCollection: CollectionReference,
    private val appDataStore: HireTopDataStoreRepos,
    private val firebaseHelper: FirebaseHelper,
    private val applicationRepos: JobOfferApplicationRepository,
) : ViewModel() {

    // Flow to hold the candidate profile id
    val candidateProfileId: Flow<String?> = appDataStore.candidateProfileId

    // StateFlow to hold the candidate profile
    private val _candidateProfile = MutableStateFlow<CandidateProfile?>(null)
    val candidateProfile: StateFlow<CandidateProfile?> = _candidateProfile

    // StateFlow to hold the recommended job offers
    private val _recommendedJobs = MutableStateFlow<List<JobOffer>?>(null)
    val recommendedJobs: StateFlow<List<JobOffer>?> = _recommendedJobs

    // StateFlow to hold the relevant job offers
    private val _jobOffers = MutableStateFlow<List<JobOffer>?>(null)
    val jobOffers: StateFlow<List<JobOffer>?> = _jobOffers

    // StateFlow to hold candidate's job applications
    private val _jobApplications = MutableStateFlow<List<JobApplication>?>(null)
    val jobApplications: StateFlow<List<JobApplication>?> = _jobApplications

    // StateFlow to hold the canApply to job offers
    private val _canApplyToJobOffer = MutableStateFlow(false)
    val canApplyToJobOffer: StateFlow<Boolean> = _canApplyToJobOffer

    private fun updateCandidateProfileId(id: String) {
        viewModelScope.launch {
            appDataStore.saveCandidateProfileIdState(newValue = id)
        }
    }

    fun getCandidateJobApplications(
        profileId: String,
        onSuccess: (List<JobApplication>?) -> Unit,
        onFailure: (String) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            applicationRepos.getJobOfferApplicationsForCandidate(
                candidateID = profileId,
                onSuccess = { items ->
                    _jobApplications.value = items
                    onSuccess(items)
                },
                onFailure = { message -> onFailure(message) })
        }
    }

    /**
     * Function to fetch job offers based on candidate skills
     */
    fun getRecommendedJobs(
        candidateSkills: List<String>,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            jobOffersCollection
                .whereArrayContainsAny("skills", candidateSkills)
                .orderBy("postedAt", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener { snapshot ->
                    val jobOffers = mutableListOf<JobOffer>()
                    for (document in snapshot.documents) {
                        val jobOffer = document.toObject(JobOffer::class.java)
                        jobOffer?.let {
                            jobOffers.add(it)
                        }
                    }

                    _recommendedJobs.value = jobOffers.filter { !it.isClosed }.take(3)

                    onSuccess()
                }
                .addOnFailureListener {
                    onFailure(
                        it.message ?: appContext.getString(R.string.read_job_offers_failure_text)
                    )
                }
        }
    }

    /**
     * Function to fetch relevant job offers based on candidate skills
     */
    fun getAllRelevantJobs(
        candidateSkills: List<String>,
        onSuccess: (List<JobOffer>) -> Unit,
        onFailure: (String) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            jobOffersCollection
                .whereArrayContainsAny("skills", candidateSkills)
                .orderBy("postedAt", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener { snapshot ->
                    val mJobOffers = mutableListOf<JobOffer>()
                    for (document in snapshot.documents) {
                        val mJobOffer = document.toObject(JobOffer::class.java)
                        mJobOffer?.let {
                            mJobOffers.add(it)
                        }
                    }
                    _jobOffers.update { mJobOffers.filter { !it.isClosed } } // Filter out closed jobs
                    onSuccess(mJobOffers)
                }
                .addOnFailureListener {
                    onFailure(
                        it.message ?: appContext.getString(R.string.read_job_offers_failure_text)
                    )
                }
        }
    }

    /**
     * Function to create a new candidate profile in Firestore
     * @param candidateProfile The candidate profile to be added
     * @param callback Callback to return the document ID generated by Firestore
     */
    fun createNewProfile(
        candidateProfile: CandidateProfile,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            candidateProfilesCollection
                .add(candidateProfile.copy(createdAt = System.currentTimeMillis()))
                .addOnSuccessListener { doc ->
                    _candidateProfile.value = candidateProfile.copy(profileId = doc.id)
                    updateCandidateProfileId(id = doc.id)
                    onSuccess()
                }
                .addOnFailureListener {
                    onFailure(
                        it.message ?: it.localizedMessage
                        ?: appContext.getString(R.string.create_candidate_profile_failure_text)
                    )
                }
        }
    }

    /**
     * Function to update candidate profile
     */
    fun updateCandidateProfile(
        profileId: String,
        editedProfile: CandidateProfile,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            candidateProfilesCollection
                .document(profileId)
                .set(editedProfile.copy(updatedAt = System.currentTimeMillis()), SetOptions.merge())
                .addOnSuccessListener {
                    _candidateProfile.value = editedProfile.copy(profileId = profileId)
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

    /**
     * Function to get candidate profile by profile ID
     */
    fun getCandidateProfile(
        profileId: String,
        onSuccess: (CandidateProfile?) -> Unit,
        onFailure: (String) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            candidateProfilesCollection.document(profileId)
                .get()
                .addOnSuccessListener { document ->
                    val profile = document.toObject(CandidateProfile::class.java)
                    _candidateProfile.value = profile
                    onSuccess(profile)
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

    /**
     * Function to increment view count of job offer
     */
    fun incrementJobOfferViewCount(
        jobOfferId: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            jobOffersCollection.document(jobOfferId)
                .update("viewCount", FieldValue.increment(1))
                .addOnSuccessListener {
                    onSuccess()
                }
                .addOnFailureListener {
                    onFailure(
                        it.message
                            ?: appContext.getString(R.string.increase_offer_view_failure_text)
                    )
                }
        }
    }

    fun canApplyToJobOffer(candidateId: String, jobOfferId: String, onFailure: (String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                jobApplicationsCollection
                    .whereEqualTo("candidateProfileId", candidateId)
                    .whereEqualTo("jobOfferId", jobOfferId)
                    .get()
                    .addOnSuccessListener { snapshot ->
                        updateCanApplyToJobOffer(snapshot.isEmpty)
                    }
                    .addOnFailureListener {
                        onFailure(
                            it.message
                                ?: appContext.getString(R.string.check_job_application_status_failure_text)
                        )
                    }

            } catch (e: Exception) {
                Log.d("canApplyToJobOffer", "${e.message}")
            }
        }
    }

    fun updateCanApplyToJobOffer(value: Boolean) {
        _canApplyToJobOffer.update { value }
    }

    fun applyToJobOffer(
        jobOffer: JobOffer,
        candidateProfile: CandidateProfile,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val jobApplication = JobApplication(
                candidateProfileId = candidateProfile.profileId,
                enterpriseProfileId = jobOffer.enterpriseID,
                jobOfferId = "${jobOffer.jobOfferID}",
                jobOfferTitle = jobOffer.title,
                companyName = jobOffer.company ?: "",
                candidateFullName = candidateProfile.name,
                candidatePictureUrl = candidateProfile.pictureUrl,
                location = jobOffer.location ?: "",
                locationType = jobOffer.locationType ?: "",
                status = appContext.getString(R.string.on_hold_application_status_text), // Set initial status
                stages = "", // Set initial stages
                appliedAt = System.currentTimeMillis() // Set application timestamp
            )

            // Adding the job application to Firestore
            jobApplicationsCollection
                .add(jobApplication)
                .addOnSuccessListener {
                    _canApplyToJobOffer.value = false
                    onSuccess()
                }
                .addOnFailureListener { exception ->
                    _canApplyToJobOffer.value = true
                    onFailure(
                        exception.message
                            ?: appContext.getString(R.string.send_job_application_failure_text)
                    )
                }
        }
    }

    fun withdrawJobApplication(
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
                            ?: appContext.getString(R.string.withdraw_job_application_failure_info)
                    )
                }
        }
    }
}