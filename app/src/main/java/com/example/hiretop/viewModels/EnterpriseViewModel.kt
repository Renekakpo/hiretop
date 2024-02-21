package com.example.hiretop.viewModels

import androidx.lifecycle.ViewModel
import com.example.hiretop.models.EnterpriseProfile
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class EnterpriseViewModel : ViewModel() {

    private val db: FirebaseFirestore by lazy { Firebase.firestore }

    fun addOrUpdateEnterpriseProfile(enterpriseId: String, profile: EnterpriseProfile) {
        db.collection("enterprises").document(enterpriseId)
            .set(profile)
            .addOnSuccessListener {
                // Handle success
            }
            .addOnFailureListener { e ->
                // Handle failure
            }
    }

    fun getEnterpriseProfile(enterpriseId: String, callback: (EnterpriseProfile?) -> Unit) {
        db.collection("enterprises").document(enterpriseId)
            .get()
            .addOnSuccessListener { document ->
                val profile = document.toObject(EnterpriseProfile::class.java)
                callback(profile)
            }
            .addOnFailureListener { e ->
                // Handle failure
                callback(null)
            }
    }

    fun addOrEditBannerImage(enterpriseId: String, bannerUrl: String) {
        db.collection("enterprises").document(enterpriseId)
            .update("bannerUrl", bannerUrl)
            .addOnSuccessListener {
                // Handle success
            }
            .addOnFailureListener { e ->
                // Handle failure
            }
    }

    fun addOrEditProfilePicture(enterpriseId: String, pictureUrl: String) {
        db.collection("enterprises").document(enterpriseId)
            .update("pictureUrl", pictureUrl)
            .addOnSuccessListener {
                // Handle success
            }
            .addOnFailureListener { e ->
                // Handle failure
            }
    }
}