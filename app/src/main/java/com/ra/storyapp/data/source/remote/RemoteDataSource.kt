package com.ra.storyapp.data.source.remote

import com.ra.storyapp.data.source.remote.network.ApiResponse
import com.ra.storyapp.data.source.remote.network.ApiService
import com.ra.storyapp.data.source.remote.network.response.*
import com.ra.storyapp.utils.reduceFileImage
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.lang.Exception

class RemoteDataSource(
    private val apiService: ApiService
): IRemoteDataSource {

    override suspend fun getAllStories(authorization: String): Flow<ApiResponse<List<StoriesResponse>>> =
        flow {
            try {
                val response = apiService.getAllStories(authorization)
                val result = response.listStory
                emit(ApiResponse.Success(result))
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.message.toString()))
            }
        }.flowOn(IO)

    override suspend fun registerAccount(
        name: String,
        email: String,
        password: String,
    ): Flow<ApiResponse<RegisterResponse>> =
        flow {
            try {
                val response = apiService.registerAccount(name, email, password)
                if(!response.error) emit(ApiResponse.Success(response))
                else emit(ApiResponse.Error(response.message))
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.message.toString()))
            }
        }.flowOn(IO)

    override suspend fun login(email: String, password: String): Flow<ApiResponse<LoginResultResponse>> =
        flow {
            try {
                val response = apiService.login(email, password)
                emit(
                    if(!response.error) ApiResponse.Success(response.loginResult)
                    else ApiResponse.Empty
                )
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.message.toString()))
            }
        }.flowOn(IO)

    override suspend fun addNewStory(
        authorization: String,
        file: File,
        description: String,
    ): Flow<ApiResponse<FileUploadResponse>> =
        flow {
            try {
                val newFile = reduceFileImage(file)
                val requestImageFile = newFile.asRequestBody("image/jpg".toMediaType())
                val imageMultiPart: MultipartBody.Part = MultipartBody.Part.createFormData(
                    "photo",
                    file.name,
                    requestImageFile
                )
                val response = apiService.addNewStory(
                    authorization,
                    imageMultiPart,
                    description.toRequestBody("text/plain".toMediaTypeOrNull())
                )

                emit(
                    if(!response.error) ApiResponse.Success(response)
                    else ApiResponse.Empty
                )

            } catch (e: Exception) {
                emit(ApiResponse.Error(e.message.toString()))
            }
        }.flowOn(IO)

}