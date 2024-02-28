package com.example.hiretop.viewModels

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hiretop.R
import com.example.hiretop.app.HireTop.Companion.appContext
import com.example.hiretop.data.datastore.HireTopDataStoreRepos
import com.example.hiretop.helpers.NetworkHelper
import com.google.android.gms.auth.api.Auth.GoogleSignInApi
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the main functionality of the application.
 * Handles user authentication, network connectivity, and user data storage.
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    private val appDataStore: HireTopDataStoreRepos,
    private val networkHelper: NetworkHelper,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    // Current user authenticated with Firebase
    val currentUser = firebaseAuth.currentUser

    // Flow representing network connectivity status
    val isNetworkAvailable: Flow<Boolean> = flow {
        emit(networkHelper.isConnectedToInternet())
    }.flowOn(Dispatchers.IO)

    // Flow representing if the current user has an enterprise account
    val isEnterpriseAccount: Flow<Boolean?> = appDataStore.isEnterpriseAccount

    /**
     * Saves the user's account type to the data store.
     * @param value Boolean indicating if the user has an enterprise account.
     */
    fun saveAccountType(value: Boolean) {
        viewModelScope.launch {
            appDataStore.saveIsEnterpriseAccountState(value)
        }
    }

    /**
     * Performs user login using email and password authentication.
     * @param email User's email address.
     * @param password User's password.
     * @param onSuccess Callback function to be executed on successful login.
     * @param onFailure Callback function to be executed on login failure.
     */
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

    /**
     * Generates the intent for Google Sign-In.
     * @return Intent for Google Sign-In.
     */
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

    /**
     * Handles the result of Google Sign-In.
     * @param data Intent data containing Google Sign-In result.
     * @param onSuccess Callback function to be executed on successful sign-in.
     * @param onFailure Callback function to be executed on sign-in failure.
     */
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

    /**
     * Authenticates with Firebase using the Google ID token.
     * @param idToken Google ID token obtained from Google Sign-In.
     * @param onSuccess Callback function to be executed on successful authentication.
     * @param onFailure Callback function to be executed on authentication failure.
     */
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

    /**
     * Signs up a new user to Firebase using email and password.
     * @param email User's email address.
     * @param password User's password.
     * @param onSuccess Callback function to be executed on successful sign-up.
     * @param onFailure Callback function to be executed on sign-up failure.
     */
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

    /**
     * Initiates the password reset process for a user.
     * @param email User's email address.
     * @param onSuccess Callback function to be executed on successful password reset.
     * @param onFailure Callback function to be executed on password reset failure.
     */
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