package com.ra.storyapp.data.source.remote

import com.ra.storyapp.data.source.remote.network.ApiResponse
import com.ra.storyapp.data.source.remote.network.response.*
import kotlinx.coroutines.flow.Flow
import java.io.File

interface IRemoteDataSource {

    suspend fun getAllStories(authorization: String): Flow<ApiResponse<List<StoriesResponse>>>

    suspend fun getAllStoriesWithLocation(authorization: String): Flow<ApiResponse<List<StoriesResponse>>>

    suspend fun registerAccount(name: String, email: String, password: String): Flow<ApiResponse<RegisterResponse>>

    suspend fun login(email: String, password: String): Flow<ApiResponse<LoginResultResponse>>

    suspend fun addNewStory(authorization: String, file: File, description: String, latitude: Float?,
                            longitude: Float?): Flow<ApiResponse<FileUploadResponse>>
}