package com.example.hiretop.di.repository

import com.example.hiretop.R
import com.example.hiretop.app.HireTop.Companion.appContext
import com.example.hiretop.models.JobApplication
import com.example.hiretop.utils.Constant
import com.google.firebase.firestore.CollectionReference
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

/**
 * Repository class for managing job offer applications.
 *
 * This class provides methods to retrieve job offer applications for candidates and companies
 * from the Firestore database.
 */
@Singleton
class JobOfferApplicationRepository @Inject constructor(
    @Named(Constant.JOB_APPLICATIONS_COLLECTION_NAME)
    private val jobApplicationsCollection: CollectionReference
) {
    /**
     * Retrieves job offer applications for a given candidate.
     *
     * @param candidateID The ID of the candidate whose job applications are to be retrieved.
     * @param onSuccess Callback function to be invoked upon successful retrieval of job applications.
     * @param onFailure Callback function to be invoked upon failure to retrieve job applications.
     */
    fun getJobOfferApplicationsForCandidate(
        candidateID: String,
        onSuccess: (List<JobApplication>?) -> Unit,
        onFailure: (String) -> Unit
    ) {
        jobApplicationsCollection
            .whereEqualTo("candidateProfileId", candidateID)
            .whereEqualTo("withdraw", false)
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

    /**
     * Retrieves job offer applications for a given company.
     *
     * @param enterpriseProfileId The ID of the company whose job applications are to be retrieved.
     * @param onSuccess Callback function to be invoked upon successful retrieval of job applications.
     * @param onFailure Callback function to be invoked upon failure to retrieve job applications.
     */
    fun getJobOfferApplicationsForCompany(
        enterpriseProfileId: String,
        onSuccess: (List<JobApplication>) -> Unit,
        onFailure: (String) -> Unit
    ) {
        jobApplicationsCollection
            .whereEqualTo("enterpriseProfileId", enterpriseProfileId)
            .whereEqualTo("withdraw", false)
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
}