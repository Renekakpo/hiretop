package com.example.hiretop.data.datastore

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
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
        val ACCOUNT_TYPE_KEY = stringPreferencesKey(name = "account_type")
        val ENTERPRISE_PROFILE_ID = stringPreferencesKey(name = "enterprise_profile_id")
        val TALENT_PROFILE_ID = stringPreferencesKey(name = "talent_profile_id")
    }

    val accountType: Flow<String> = dataStore.data
        .catch {
            if (it is IOException) {
                Log.d(TAG, "Error reading preferences", it)
                emit(value = emptyPreferences())
            } else {
                throw it
            }
        }.map { prefs -> prefs[ACCOUNT_TYPE_KEY] ?: "" }

    suspend fun saveAccountTypeState(accountType: String) {
        dataStore.edit { prefs -> prefs[ACCOUNT_TYPE_KEY] = accountType }
    }

    val enterpriseProfileID: Flow<String> = dataStore.data
        .catch {
            if (it is IOException) {
                Log.d(TAG, "Error reading preferences", it)
                emit(value = emptyPreferences())
            } else {
                throw it
            }
        }.map { prefs -> prefs[ENTERPRISE_PROFILE_ID] ?: "" }

    suspend fun saveEnterpriseProfileIDState(accountType: String) {
        dataStore.edit { prefs -> prefs[ENTERPRISE_PROFILE_ID] = accountType }
    }

    val talentProfileID: Flow<String> = dataStore.data
        .catch {
            if (it is IOException) {
                Log.d(TAG, "Error reading preferences", it)
                emit(value = emptyPreferences())
            } else {
                throw it
            }
        }.map { prefs -> prefs[TALENT_PROFILE_ID] ?: "" }

    suspend fun saveTalentProfileIDState(accountType: String) {
        dataStore.edit { prefs -> prefs[TALENT_PROFILE_ID] = accountType }
    }
}