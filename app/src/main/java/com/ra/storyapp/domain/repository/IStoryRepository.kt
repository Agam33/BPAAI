package com.ra.storyapp.domain.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.ra.storyapp.data.source.local.database.entity.StoryEntity
import com.ra.storyapp.data.source.remote.network.response.FileUploadResponse
import com.ra.storyapp.data.source.remote.network.response.RegisterResponse
import com.ra.storyapp.domain.model.LoginResult
import com.ra.storyapp.utils.Resources
import kotlinx.coroutines.flow.Flow
import java.io.File

interface IStoryRepository {
//     fun getAllStoriesWithLocation(authorization: String, page: Int, size: Int): Flow<Resources<List<StoriesResponse>>>

     fun getAllStories(): LiveData<PagingData<StoryEntity>>

     fun register(name: String, email: String, password: String): Flow<Resources<RegisterResponse>>

     fun login(email: String, password: String): Flow<Resources<LoginResult>>

     fun addNewStory(authorization: String, file: File, description: String): Flow<Resources<FileUploadResponse>>
}