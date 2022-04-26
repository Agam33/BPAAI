package com.ra.storyapp.data

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.ra.storyapp.data.source.StoryRemoteMediator
import com.ra.storyapp.data.source.local.ILocalDataSource
import com.ra.storyapp.data.source.local.database.entity.StoryEntity
import com.ra.storyapp.data.source.remote.IRemoteDataSource
import com.ra.storyapp.data.source.remote.network.ApiResponse
import com.ra.storyapp.data.source.remote.network.response.FileUploadResponse
import com.ra.storyapp.data.source.remote.network.response.RegisterResponse
import com.ra.storyapp.data.source.remote.network.response.StoriesResponse
import com.ra.storyapp.domain.model.LoginResult
import com.ra.storyapp.domain.model.Story
import com.ra.storyapp.domain.repository.IStoryRepository
import com.ra.storyapp.utils.DataMapper
import com.ra.storyapp.utils.Resources
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import java.io.File

@OptIn(ExperimentalPagingApi::class)
class StoryRepository(
    private val remote: IRemoteDataSource,
    private val local: ILocalDataSource
): IStoryRepository {

    override fun getAllStories(): LiveData<PagingData<StoryEntity>> =
        Pager(
            config = PagingConfig(
                pageSize = 7
            ),
            remoteMediator = StoryRemoteMediator(),
            pagingSourceFactory = {
                local.getStories()
            }
        ).liveData

    override fun getAllStoriesWithLocation(authorization: String): Flow<Resources<List<Story>>> =
        flow {
            emit(Resources.Loading())
            val stories = ArrayList<Story>()
            when(val apiResponse = remote.getAllStories(authorization).first()) {
                is ApiResponse.Success -> {
                    apiResponse.data.map {
                        val data = DataMapper.storyResponseToModel(it)
                        stories.add(data)
                    }
                    emit(Resources.Success(stories))
                }
                is ApiResponse.Empty -> {
                    emit(Resources.Success(stories))
                }
                is ApiResponse.Error -> {
                    emit(Resources.Error(apiResponse.errorMsg))
                }
            }
        }

    override fun register(
        name: String,
        email: String,
        password: String
    ): Flow<Resources<RegisterResponse>> =
        flow {
            emit(Resources.Loading())
            when(val apiResponse = remote.registerAccount(name, email, password).first()) {
                is ApiResponse.Success -> {
                    val data = apiResponse.data
                    emit(Resources.Success(data))
                }
                is ApiResponse.Error -> {
                    emit(Resources.Error(apiResponse.errorMsg))
                }
                else -> {}
            }
        }

    override fun login(email: String, password: String): Flow<Resources<LoginResult>> =
        flow {
            emit(Resources.Loading())
            when(val apiResponse = remote.login(email, password).first()) {
                is ApiResponse.Success -> {
                    val data = DataMapper.loginResultResponseToModel(apiResponse.data)
                    emit(Resources.Success(data))
                }
                is ApiResponse.Empty -> {
                    emit(Resources.Error(""))
                }
                is ApiResponse.Error -> {
                   emit(Resources.Error(apiResponse.errorMsg))
                }
            }
        }

    override fun addNewStory(
        authorization: String,
        file: File,
        description: String,
        latitude: Float?,
        longitude: Float?
    ): Flow<Resources<FileUploadResponse>> =
        flow {
            emit(Resources.Loading())
            when(val apiResponse = remote.addNewStory(authorization, file, description, latitude, longitude).first()) {
                is ApiResponse.Success -> {
                    emit(Resources.Success(apiResponse.data))
                }
                is ApiResponse.Error -> {
                    emit(Resources.Error(apiResponse.errorMsg))
                }
                else -> {}
            }
        }
}