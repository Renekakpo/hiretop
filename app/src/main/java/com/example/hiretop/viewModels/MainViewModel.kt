package com.example.hiretop.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hiretop.data.datastore.HireTopDataStoreRepos
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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

    fun loginWithEmailAndPassword(email: String, password: String) {
        TODO("Implement firebase login using email and password")
    }

    fun loginWithGoogleAccount() {
        TODO("Implement firebase login using google account")
    }

    fun loginWithGithubAccount() {
        TODO("Implement firebase login using github account")
    }

    fun signUpToFirebaseWithEmailAndPassword() {
        TODO("Implement firebase signup using github account")
    }

    fun forgotPasswordProcess() {
        TODO("Implement firebase's forgot password case")
    }
}