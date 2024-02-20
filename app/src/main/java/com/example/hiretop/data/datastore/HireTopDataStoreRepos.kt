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

    suspend fun saveOnBoardingCompletionState(accountType: String) {
        dataStore.edit { prefs -> prefs[ACCOUNT_TYPE_KEY] = accountType }
    }
}