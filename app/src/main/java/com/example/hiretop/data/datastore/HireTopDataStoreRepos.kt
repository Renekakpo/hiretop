package com.example.hiretop.data.datastore

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException


class HireTopDataStoreRepos(private val dataStore: DataStore<Preferences>) {
    private companion object {
        val TAG: String = HireTopDataStoreRepos::class.java.simpleName
        val IS_ENTERPRISE_ACCOUNT_KEY = booleanPreferencesKey(name = "is_enterprise_account")
        val ENTERPRISE_PROFILE_ID = stringPreferencesKey(name = "enterprise_profile_id")
        val CANDIDATE_PROFILE_ID = stringPreferencesKey(name = "candidate_profile_id")
    }

    val isEnterpriseAccount: Flow<Boolean?> = dataStore.data
        .catch {
            if (it is IOException) {
                Log.d(TAG, "Error reading preferences", it)
                emit(value = emptyPreferences())
            } else {
                throw it
            }
        }.map { prefs -> prefs[IS_ENTERPRISE_ACCOUNT_KEY] }

    suspend fun saveIsEnterpriseAccountState(newValue: Boolean) {
        dataStore.edit { prefs -> prefs[IS_ENTERPRISE_ACCOUNT_KEY] = newValue }
    }

    val enterpriseProfileId: Flow<String> = dataStore.data
        .catch {
            if (it is IOException) {
                Log.d(TAG, "Error reading preferences", it)
                emit(value = emptyPreferences())
            } else {
                throw it
            }
        }.map { prefs -> prefs[ENTERPRISE_PROFILE_ID] ?: "" }

    suspend fun saveEnterpriseProfileIdState(newValue: String) {
        dataStore.edit { prefs -> prefs[ENTERPRISE_PROFILE_ID] = newValue }
    }

    val candidateProfileId: Flow<String?> = dataStore.data
        .catch {
            if (it is IOException) {
                Log.d(TAG, "Error reading preferences", it)
                emit(value = emptyPreferences())
            } else {
                throw it
            }
        }.map { prefs -> prefs[CANDIDATE_PROFILE_ID] }

    suspend fun saveCandidateProfileIdState(newValue: String) {
        dataStore.edit { prefs -> prefs[CANDIDATE_PROFILE_ID] = newValue }
    }
}