package com.ra.storyapp.utils.preferences

import kotlinx.coroutines.flow.Flow

interface IUserPreferencesStore {
    suspend fun saveVerification(verify: Boolean?)
    fun isVerified(): Flow<Boolean?>
    suspend fun saveToken(token: String?)
    fun getToken(): Flow<String?>
    suspend fun clearData()
}