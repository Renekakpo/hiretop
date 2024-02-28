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

/**
 * ViewModel class for managing candidate-related data and operations.
 * @property db Instance of FirebaseFirestore for database operations.
 * @property jobOffersCollection Reference to the collection of job offers in Firestore.
 * @property candidateProfilesCollection Reference to the collection of candidate profiles in Firestore.
 * @property jobApplicationsCollection Reference to the collection of job applications in Firestore.
 * @property appDataStore Instance of HireTopDataStoreRepos for managing candidate data locally.
 * @property firebaseHelper Instance of FirebaseHelper for Firebase-related operations.
 * @property applicationRepos Instance of JobOfferApplicationRepository for managing job application data.
 */
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

    /**
     * Function to update the candidate profile ID in local data store.
     * @param id The new candidate profile ID to be saved.
     */
    private fun updateCandidateProfileId(id: String) {
        viewModelScope.launch {
            appDataStore.saveCandidateProfileIdState(newValue = id)
        }
    }

    /**
     * Function to fetch job applications for a candidate.
     * @param profileId The ID of the candidate's profile.
     * @param onSuccess Callback invoked on successful retrieval of job applications.
     * @param onFailure Callback invoked on failure to retrieve job applications.
     */
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
     * Function to fetch recommended job offers based on candidate skills.
     * @param candidateSkills List of candidate's skills.
     * @param onSuccess Callback invoked on successful retrieval of recommended job offers.
     * @param onFailure Callback invoked on failure to retrieve recommended job offers.
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
     * Function to fetch all relevant job offers based on candidate skills.
     * @param candidateSkills List of candidate's skills.
     * @param onSuccess Callback invoked on successful retrieval of relevant job offers.
     * @param onFailure Callback invoked on failure to retrieve relevant job offers.
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
     * Function to create a new candidate profile in Firestore.
     * @param candidateProfile The candidate profile to be added.
     * @param onSuccess Callback invoked on successful creation of the candidate profile.
     * @param onFailure Callback invoked on failure to create the candidate profile.
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
     * Function to update candidate profile in Firestore.
     * @param profileId The ID of the candidate's profile.
     * @param editedProfile The edited candidate profile.
     * @param onSuccess Callback invoked on successful update of the candidate profile.
     * @param onFailure Callback invoked on failure to update the candidate profile.
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
                    updateCandidateJobApplicationDetails(editedProfile)
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
     * Function to get candidate profile by profile ID.
     * @param profileId The ID of the candidate's profile.
     * @param onSuccess Callback invoked on successful retrieval of the candidate profile.
     * @param onFailure Callback invoked on failure to retrieve the candidate profile.
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

    /**
     * Function to upload a file to Firebase Storage and get the download URL.
     * @param inputStream Input stream of the file to be uploaded.
     * @param fileName Name of the file.
     * @param onSuccess Callback invoked on successful upload of the file.
     * @param onFailure Callback invoked on failure to upload the file.
     */
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
     * Function to increment the view count of a job offer.
     * @param jobOfferId The ID of the job offer.
     * @param onSuccess Callback invoked on successful increment of the view count.
     * @param onFailure Callback invoked on failure to increment the view count.
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

    /**
     * Function to check if a candidate can apply to a job offer.
     * @param candidateId The ID of the candidate.
     * @param jobOfferId The ID of the job offer.
     * @param onFailure Callback invoked on failure to check application status.
     */
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

    /**
     * Function to update the canApplyToJobOffer StateFlow value.
     * @param value The new value for canApplyToJobOffer.
     */
    fun updateCanApplyToJobOffer(value: Boolean) {
        _canApplyToJobOffer.update { value }
    }

    /**
     * Function to apply to a job offer.
     * @param jobOffer The job offer to apply to.
     * @param candidateProfile The candidate's profile.
     * @param onSuccess Callback invoked on successful application.
     * @param onFailure Callback invoked on failure to apply to the job offer.
     */
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
                stages = null, // Set initial stages
                appliedAt = System.currentTimeMillis(), // Set application timestamp
                interviewDate = null,
                offerContent = null
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

    /**
     * Function to withdraw a job application.
     * @param jobApplication The job application to withdraw.
     * @param onSuccess Callback invoked on successful withdrawal of the job application.
     * @param onFailure Callback invoked on failure to withdraw the job application.
     */
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

    /**
     * Function to update candidate job application details after profile update.
     * @param updatedProfile The updated candidate profile.
     */
    private fun updateCandidateJobApplicationDetails(updatedProfile: CandidateProfile) {
        try {
            jobApplicationsCollection
                .whereEqualTo("candidateProfileId", updatedProfile.profileId)
                .get()
                .addOnSuccessListener { snapshot ->
                    if (!snapshot.isEmpty) { // If candidate has job applications
                        val updatedCandidacies = mutableListOf<JobApplication>()

                        snapshot.forEach { item ->
                            val candidacy = item.toObject(JobApplication::class.java)
                            val candidacyCopy = candidacy.copy(
                                candidateFullName = updatedProfile.name,
                                candidatePictureUrl = updatedProfile.pictureUrl
                            )
                            updatedCandidacies.add(candidacyCopy)
                        }

                        viewModelScope.launch(Dispatchers.IO){
                            updatedCandidacies.forEach { candidacy ->
                                jobApplicationsCollection
                                    .document(candidacy.jobApplicationId ?: "")
                                    .set(candidacy, SetOptions.merge())
                                    .await()
                            }
                        }
                    }
                }
                .addOnFailureListener {
                    Log.d("updateCandidateJobApplicationDetails", "${it.message}")
                }
        } catch (e: Exception) {
            Log.d("updateCandidateJobApplicationDetails", "${e.message}")
        }
    }
}