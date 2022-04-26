package com.ra.storyapp.domain.repository

import com.ra.storyapp.data.source.remote.network.response.FileUploadResponse
import com.ra.storyapp.data.source.remote.network.response.RegisterResponse
import com.ra.storyapp.domain.model.LoginResult
import com.ra.storyapp.domain.model.Story
import com.ra.storyapp.utils.Resources
import kotlinx.coroutines.flow.Flow
import java.io.File

interface IStoryRepository {

     fun getAllStories(authorization: String): Flow<Resources<List<Story>>>

     fun getAllStoriesWithLocation(authorization: String): Flow<Resources<List<Story>>>

     fun register(name: String, email: String, password: String): Flow<Resources<RegisterResponse>>

     fun login(email: String, password: String): Flow<Resources<LoginResult>>

     fun addNewStory(authorization: String, file: File, description: String, latitude: Float?,
                     longitude: Float?): Flow<Resources<FileUploadResponse>>
}