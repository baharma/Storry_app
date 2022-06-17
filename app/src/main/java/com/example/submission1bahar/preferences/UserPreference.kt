package com.example.submission1bahar.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreference private constructor(private val dataStore: DataStore<Preferences>) {


    private val tokenKeys = stringPreferencesKey("token_key")


    fun getUserAuth(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[tokenKeys] ?: ""
        }
    }

    suspend fun saveUserAuth(token: String) {
        dataStore.edit { preferences ->
            preferences[tokenKeys] = token
        }
    }

    suspend fun removeUserAuth() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null

        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}
