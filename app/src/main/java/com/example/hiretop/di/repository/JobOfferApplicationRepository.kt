package com.example.hiretop.di.repository

import androidx.compose.animation.core.snap
import com.example.hiretop.R
import com.example.hiretop.app.HireTop.Companion.appContext
import com.example.hiretop.models.CandidateProfile
import com.example.hiretop.models.JobApplication
import com.example.hiretop.utils.Constant
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class JobOfferApplicationRepository @Inject constructor(
    private val db: FirebaseFirestore,
    @Named(Constant.JOB_APPLICATIONS_COLLECTION_NAME)
    private val jobApplicationsCollection: CollectionReference,
    @Named(Constant.CANDIDATES_COLLECTION_NAME)
    private val provideCandidateProfilesCollection: CollectionReference,
) {

    // Function to create a new job offer application
    suspend fun createJobOfferApplication(
        jobApplication: JobApplication,
        onSuccess: (String) -> Unit,
        onFailure: (String) -> Unit
    ) {
        jobApplicationsCollection
            .add(jobApplication)
            .addOnSuccessListener {
                onSuccess(it.id)
            }
            .addOnFailureListener {
                onFailure(
                    it.message
                        ?: appContext.getString(R.string.job_application_creation_failed_text)
                )
            }
    }

    // Function to update an existing job offer application
    suspend fun updateJobOfferApplication(
        jobApplication: JobApplication,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        jobApplication.jobApplicationId?.let { applicationId ->
            jobApplicationsCollection
                .document(applicationId)
                .set(jobApplication, SetOptions.merge())
                .addOnSuccessListener {
                    onSuccess()
                }
                .addOnFailureListener {
                    onFailure(
                        it.message
                            ?: appContext.getString(R.string.read_job_application_failed_text)
                    )
                }
        }
    }

    // Function to delete a job offer application
    suspend fun deleteJobOfferApplication(
        jobApplicationID: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        jobApplicationsCollection
            .document(jobApplicationID)
            .delete()
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener {
                onFailure(
                    it.message ?: appContext.getString(R.string.read_job_application_failed_text)
                )
            }
    }

    // Function to retrieve job offer applications for a specific candidate
    suspend fun getJobOfferApplicationsForCandidate(
        candidateID: String,
        onSuccess: (List<JobApplication>?) -> Unit,
        onFailure: (String) -> Unit
    ) {
        jobApplicationsCollection
            .whereEqualTo("candidateID", candidateID)
            .get()
            .addOnSuccessListener { snapshot ->
                val applications = mutableListOf<JobApplication>()

                if (snapshot.documents.isNotEmpty()) {
                    for (document in snapshot.documents) {
                        val jobApplication = document.toObject(JobApplication::class.java)
                        jobApplication?.let { applications.add(it) }
                    }
                }

                onSuccess(applications)
            }
            .addOnFailureListener {
                onFailure(
                    it.message ?: appContext.getString(R.string.read_job_application_failed_text)
                )
            }
    }

    // Function to retrieve job offer applications for a specific company
    suspend fun getJobOfferApplicationsForCompany(
        enterpriseProfileId: String,
        companyName: String,
        onSuccess: (List<JobApplication>) -> Unit,
        onFailure: (String) -> Unit
    ) {
        jobApplicationsCollection
            .whereEqualTo("companyName", companyName)
            .whereEqualTo("enterpriseProfileId", enterpriseProfileId)
            .get()
            .addOnSuccessListener { snapshot ->
                val applications = mutableListOf<JobApplication>()

                if (snapshot.documents.isNotEmpty()) {
                    for (document in snapshot.documents) {
                        val jobApplication = document.toObject(JobApplication::class.java)
                        jobApplication?.let { applications.add(it) }
                    }
                }

                onSuccess(applications)
            }
            .addOnFailureListener {
                onFailure(
                    it.message ?: appContext.getString(R.string.read_job_application_failed_text)
                )
            }
    }

    // Function to retrieve job offer applications for a specific jobOffer
    suspend fun getJobOfferApplicationsForJobOffer(
        jobOfferId: String,
        onSuccess: (List<JobApplication>) -> Unit,
        onFailure: (String) -> Unit
    ) {
        jobApplicationsCollection
            .whereEqualTo("jobOfferID", jobOfferId)
            .get()
            .addOnSuccessListener { snapshot ->
                val applications = mutableListOf<JobApplication>()

                if (snapshot.documents.isNotEmpty()) {
                    for (document in snapshot.documents) {
                        val jobApplication = document.toObject(JobApplication::class.java)
                        jobApplication?.let { applications.add(it) }
                    }
                }

                onSuccess(applications)
            }
            .addOnFailureListener {
                onFailure(
                    it.message ?: appContext.getString(R.string.read_job_application_failed_text)
                )
            }
    }

    suspend fun getCandidateProfileFromApplicationData(
        candidateProfileId: String,
        onSuccess: (CandidateProfile?) -> Unit,
        onFailure: (String) -> Unit
    ) {
        provideCandidateProfilesCollection
            .document(candidateProfileId)
            .get()
            .addOnSuccessListener { snapshot ->
                val profile = snapshot.toObject(CandidateProfile::class.java)
                onSuccess(profile)
            }
            .addOnFailureListener {
                onFailure(it.message ?: appContext.getString(R.string.read_profile_failure_text))
            }
    }
}