package com.ra.storyapp.utils.preferences

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore("user_preferences")

class UserPreferencesStore(private val context: Context): IUserPreferencesStore {

    private val USER_TOKEN = stringPreferencesKey("user_token")
    private val IS_VERIFIED = booleanPreferencesKey("user_verify")

    override suspend fun saveVerification(verify: Boolean?) {
        context.dataStore.edit { preferences ->
            preferences[IS_VERIFIED] = verify ?: false
        }
    }

    override fun isVerified(): Flow<Boolean?> =
        context.dataStore.data.map { preferences ->
            preferences[IS_VERIFIED] ?: false
        }

    override suspend fun saveToken(token: String?) {
         context.dataStore.edit { preferences ->
            preferences[USER_TOKEN] = token ?: ""
        }
    }

    override fun getToken(): Flow<String?> =
        context.dataStore.data.map { preferences ->
            preferences[USER_TOKEN]
        }

    override suspend fun clearData() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}