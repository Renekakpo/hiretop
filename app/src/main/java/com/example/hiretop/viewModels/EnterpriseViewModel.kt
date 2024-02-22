package com.example.hiretop.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hiretop.models.EnterpriseProfile
import com.example.hiretop.models.JobOffer
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EnterpriseViewModel @Inject constructor(
    private val db: FirebaseFirestore,
    private val jobOffersCollection: CollectionReference,
    private val enterprisesCollection: CollectionReference
) : ViewModel() {

    fun addJobOffer(jobOffer: JobOffer) {
        viewModelScope.launch(Dispatchers.IO) {
            jobOffersCollection.add(jobOffer)
                .addOnSuccessListener {
                    TODO("Handle success")
                }
                .addOnFailureListener {
                    TODO("Handle failure")
                }
        }
    }

    fun updateJobOffer(jobOffer: JobOffer) {
        viewModelScope.launch(Dispatchers.IO) {
            jobOffer.jobOfferID?.let { jobOfferID ->
                jobOffersCollection.document(jobOfferID).set(jobOffer)
                    .addOnSuccessListener {
                        TODO("Handle success")
                    }
                    .addOnFailureListener {
                        TODO("Handle failure")
                    }
            }
        }
    }

    fun deleteJobOffer(jobOfferID: String) {
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

    fun getAllJobOffersForEnterprise(enterpriseID: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val querySnapshot = jobOffersCollection.whereEqualTo("enterpriseID", enterpriseID).get()
                .addOnSuccessListener {
                    TODO("Handle success")
                }
                .addOnFailureListener {
                    TODO("Handle failure")
                }
        }
    }

    /**
     * Function to get auto-generated ID by Firestore when creating a new profile
     */
    fun createNewEnterpriseProfile(profile: EnterpriseProfile, callback: (String?) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            enterprisesCollection
                .add(profile)
                .addOnSuccessListener { documentReference ->
                    callback(documentReference.id)
                    TODO("Save document id to datastore")
                }
                .addOnFailureListener {
                    callback(it.message ?: it.localizedMessage ?: "no error message")
                }
        }
    }

    fun addOrUpdateEnterpriseProfile(enterpriseId: String, profile: EnterpriseProfile) {
        viewModelScope.launch(Dispatchers.IO) {
            val enterpriseRef = enterprisesCollection.document(enterpriseId)
            if (profile.enterpriseID == null) {
                enterpriseRef.set(profile)
                    .addOnSuccessListener {
                        TODO("Handle success")
                    }
                    .addOnFailureListener {
                        TODO("Handle failure")
                    }
            } else {
                enterpriseRef.set(profile.copy(enterpriseID = null))
                    .addOnSuccessListener {
                        TODO("Handle success")
                    }
                    .addOnFailureListener {
                        TODO("Handle failure")
                    }
            }
        }
    }

    fun getEnterpriseProfile(enterpriseId: String, callback: (EnterpriseProfile?) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            enterprisesCollection.document(enterpriseId)
                .get()
                .addOnSuccessListener { document ->
                    val profile = document.toObject<EnterpriseProfile>()
                    callback(profile)
                }
                .addOnFailureListener {
                    callback(null)
                }
        }
    }

    fun addOrEditBannerImage(enterpriseId: String, bannerUrl: String, callback: (String?) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            enterprisesCollection.document(enterpriseId)
                .update("bannerUrl", bannerUrl)
                .addOnSuccessListener {
                    callback("success")
                }
                .addOnFailureListener {
                    callback(it.message ?: "no error message")
                }
        }
    }

    fun addOrEditProfilePicture(
        enterpriseId: String,
        pictureUrl: String,
        callback: (String?) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            enterprisesCollection.document(enterpriseId)
                .update("pictureUrl", pictureUrl)
                .addOnSuccessListener {
                    callback("success")
                }
                .addOnFailureListener {
                    callback(it.message ?: it.localizedMessage ?: "no error message")
                }
        }
    }
}