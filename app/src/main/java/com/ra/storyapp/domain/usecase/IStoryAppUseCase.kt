package com.ra.storyapp.domain.usecase

import com.ra.storyapp.data.source.remote.network.response.FileUploadResponse
import com.ra.storyapp.data.source.remote.network.response.RegisterResponse
import com.ra.storyapp.domain.model.LoginResult
import com.ra.storyapp.domain.model.Story
import com.ra.storyapp.utils.Resources
import kotlinx.coroutines.flow.Flow
import java.io.File

interface IStoryAppUseCase {

    fun getAllStories(authorization: String): Flow<Resources<List<Story>>>

    fun getAllStoriesWithLocation(authorization: String): Flow<Resources<List<Story>>>

    fun register(name: String, email: String, password: String): Flow<Resources<RegisterResponse>>

    fun login(email: String, password: String): Flow<Resources<LoginResult>>

    fun addNewStory(authorization: String, file: File, description: String): Flow<Resources<FileUploadResponse>>

    suspend fun saveVerification(verify: Boolean?)

    fun isVerified(): Flow<Boolean?>

    suspend fun saveToken(token: String?)

    fun getToken(): Flow<String?>

    suspend fun signOut()
}