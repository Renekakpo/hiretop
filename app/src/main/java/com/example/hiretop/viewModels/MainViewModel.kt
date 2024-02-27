package com.example.hiretop.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hiretop.data.datastore.HireTopDataStoreRepos
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val appDataStore: HireTopDataStoreRepos) :
    ViewModel() {

    val isEnterpriseAccount: Flow<Boolean?> = appDataStore.isEnterpriseAccount

    fun saveAccountType(value: Boolean) {
        viewModelScope.launch {
            appDataStore.saveIsEnterpriseAccountState(value)
        }
    }

    fun loginWithEmailAndPassword(email: String, password: String, onSuccess : () -> Unit, onFailure : (String) -> Unit) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    onFailure(task.exception?.message ?: "An error occurred")
                }
            }
    }

    fun loginWithGoogleAccount(onSuccess : () -> Unit, onFailure : (String) -> Unit) {
        TODO("Implement the complete version firebase login using google account")
    }

    fun loginWithGithubAccount(onSuccess : () -> Unit, onFailure : (String) -> Unit) {
        TODO("Implement the complete version firebase login using github account")
    }

    fun signUpToFirebaseWithEmailAndPassword(email: String, password: String, onSuccess : () -> Unit, onFailure : (String) -> Unit) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    onFailure(task.exception?.message ?: "An error occurred")
                }
            }
    }

    fun forgotPasswordProcess(email: String, onSuccess : () -> Unit, onFailure : (String) -> Unit) {
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    onFailure(task.exception?.message ?: "An error occurred")
                }
            }
    }
}