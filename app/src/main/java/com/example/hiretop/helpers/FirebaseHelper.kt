package com.example.hiretop.helpers

import androidx.lifecycle.viewModelScope
import com.example.hiretop.utils.Constant.JOB_OFFERS_COLLECTION_NAME
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.InputStream

class FirebaseHelper {

    private val db: FirebaseFirestore by lazy { Firebase.firestore }
    private val jobOffersCollection = db.collection(JOB_OFFERS_COLLECTION_NAME)

    fun uploadFileToFirebaseStorage(inputStream: InputStream, fileName: String, onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit) {
        val storageRef = Firebase.storage.reference
        val fileRef = storageRef.child("images/$fileName")
        val uploadTask = fileRef.putStream(inputStream)

        uploadTask.addOnSuccessListener { taskSnapshot ->
            // File uploaded successfully
            fileRef.downloadUrl.addOnSuccessListener { uri ->
                onSuccess(uri.toString()) // Get the download URL and pass it to the onSuccess callback
            }.addOnFailureListener {
                onFailure(it)
            }
        }.addOnFailureListener {
            // Handle unsuccessful uploads
            onFailure(it)
        }
    }

    fun getJobOffer(jobOfferID: String) {
        val snapshot = jobOffersCollection.document(jobOfferID).get()
            .addOnSuccessListener {
                TODO("Handle success")
            }
            .addOnFailureListener {
                TODO("Handle failure")
            }
    }

}