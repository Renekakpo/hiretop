package com.example.hiretop.helpers

import com.example.hiretop.R
import com.example.hiretop.app.HireTop.Companion.appContext
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.InputStream
import javax.inject.Inject

class FirebaseHelper @Inject constructor() {
    fun uploadFileToFirebaseStorage(
        inputStream: InputStream,
        fileName: String,
        onSuccess: (String) -> Unit,
        onFailure: (String) -> Unit
    ) {
        val storageRef = Firebase.storage.reference
        val fileRef = storageRef.child("images/$fileName")
        val uploadTask = fileRef.putStream(inputStream)

        uploadTask.addOnSuccessListener {
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
}