package com.example.hiretop.helpers

import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.InputStream

class FirebaseHelper {

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

}