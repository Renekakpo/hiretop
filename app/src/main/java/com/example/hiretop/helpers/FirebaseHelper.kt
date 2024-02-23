package com.example.hiretop.helpers

import androidx.lifecycle.viewModelScope
import com.example.hiretop.R
import com.example.hiretop.app.HireTop.Companion.appContext
import com.example.hiretop.models.JobOffer
import com.example.hiretop.utils.Constant.JOB_OFFERS_COLLECTION_NAME
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.InputStream
import javax.inject.Inject
import javax.inject.Named

class FirebaseHelper @Inject constructor(
    private val db: FirebaseFirestore,
    @Named(JOB_OFFERS_COLLECTION_NAME)
    private val jobOffersCollection: CollectionReference,
) {

    fun uploadFileToFirebaseStorage(
        inputStream: InputStream,
        fileName: String,
        onSuccess: (String) -> Unit,
        onFailure: (String) -> Unit
    ) {
        val storageRef = Firebase.storage.reference
        val fileRef = storageRef.child("images/$fileName")
        val uploadTask = fileRef.putStream(inputStream)

        uploadTask.addOnSuccessListener { taskSnapshot ->
            // File uploaded successfully
            fileRef.downloadUrl.addOnSuccessListener { uri ->
                onSuccess(uri.toString()) // Get the download URL and pass it to the onSuccess callback
            }.addOnFailureListener {
                onFailure(it.message ?: appContext.getString(R.string.uploaded_file_url_error_text))
            }
        }.addOnFailureListener {
            // Handle unsuccessful uploads
            onFailure(it.message ?: appContext.getString(R.string.file_upload_failed_text))
        }
    }

    fun getJobOffer(
        jobOfferID: String,
        onSuccess: (JobOffer) -> Unit,
        onFailure: (String) -> Unit
    ) {
        val snapshot = jobOffersCollection.document(jobOfferID).get()
            .addOnSuccessListener { snapshot ->
                val jobOffer = snapshot.toObject(JobOffer::class.java)
                jobOffer?.let { onSuccess(it) }
            }
            .addOnFailureListener {
                onFailure(it.message ?: appContext.getString(R.string.fetch_job_offer_error_text))
            }
    }

}