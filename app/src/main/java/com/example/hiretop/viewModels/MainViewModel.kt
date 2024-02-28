package com.example.hiretop.viewModels

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hiretop.R
import com.example.hiretop.app.HireTop.Companion.appContext
import com.example.hiretop.data.datastore.HireTopDataStoreRepos
import com.example.hiretop.helpers.NetworkHelper
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.Auth.GoogleSignInApi
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val appDataStore: HireTopDataStoreRepos,
    private val networkHelper: NetworkHelper,
    private val firebaseAuth: FirebaseAuth
) :
    ViewModel() {

    val currentUser = firebaseAuth.currentUser
    val isNetworkAvailable: Flow<Boolean> = flow {
        emit(networkHelper.isConnectedToInternet())
    }.flowOn(Dispatchers.IO)
    val isEnterpriseAccount: Flow<Boolean?> = appDataStore.isEnterpriseAccount

    fun saveAccountType(value: Boolean) {
        viewModelScope.launch {
            appDataStore.saveIsEnterpriseAccountState(value)
        }
    }

    fun loginWithEmailAndPassword(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    onFailure(task.exception?.message ?: appContext.getString(R.string.unknown_error_text))
                }
            }
    }

    // Function to obtain Google Sign-In Intent
    fun getGoogleSignInIntent(): Intent {
        // Configure Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(appContext.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        // Build a GoogleSignInClient with the options specified by gso.
        val mGoogleSignInClient = GoogleSignIn.getClient(appContext, gso)

        // Return the Google Sign-In Intent
        return mGoogleSignInClient.signInIntent
    }


    // Handle the result of the Google Sign-In Intent in the activity's onActivityResult method
    fun handleGoogleSignInResult(data: Intent?, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        val task = data?.let { GoogleSignInApi.getSignInResultFromIntent(it) }
        if (task != null && task.isSuccess) {
            // Google Sign-In was successful, authenticate with Firebase
            val account = task.signInAccount
            if (account != null) {
                firebaseAuthWithGoogle(account.idToken!!, onSuccess, onFailure)
            } else {
                onFailure("Google Sign-In account is null")
            }
        } else {
            // Google Sign-In failed
            onFailure("Google Sign-In failed")
        }
    }

    // Authenticate with Firebase using the Google ID token
    private fun firebaseAuthWithGoogle(idToken: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Firebase authentication successful
                    onSuccess()
                } else {
                    // Firebase authentication failed
                    onFailure(task.exception?.message ?: "Google Sign-In failed")
                }
            }
    }

    fun signUpToFirebaseWithEmailAndPassword(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    onFailure(task.exception?.message ?: appContext.getString(R.string.unknown_error_text))
                }
            }
    }

    fun forgotPasswordProcess(email: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    onFailure(task.exception?.message ?: appContext.getString(R.string.unknown_error_text))
                }
            }
    }
}