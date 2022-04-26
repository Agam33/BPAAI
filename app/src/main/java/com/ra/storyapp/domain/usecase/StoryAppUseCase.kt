package com.ra.storyapp.domain.usecase

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.ra.storyapp.data.source.local.database.entity.StoryEntity
import com.ra.storyapp.data.source.remote.network.response.FileUploadResponse
import com.ra.storyapp.data.source.remote.network.response.RegisterResponse
import com.ra.storyapp.domain.model.LoginResult
import com.ra.storyapp.domain.model.Story
import com.ra.storyapp.domain.repository.IStoryRepository
import com.ra.storyapp.utils.preferences.IUserPreferencesStore
import com.ra.storyapp.utils.Resources
import kotlinx.coroutines.flow.Flow
import java.io.File

class StoryAppUseCase(
    private val storyRepository: IStoryRepository,
    private val pref: IUserPreferencesStore
): IStoryAppUseCase {
    override fun getAllStories(): LiveData<PagingData<StoryEntity>> =
        storyRepository.getAllStories()

    override fun register(name: String, email: String, password: String): Flow<Resources<RegisterResponse>> =
        storyRepository.register(name, email, password)

    override fun login(email: String, password: String): Flow<Resources<LoginResult>> =
        storyRepository.login(email, password)

    override fun addNewStory(authorization: String, file: File, description: String): Flow<Resources<FileUploadResponse>> =
        storyRepository.addNewStory(authorization, file, description)

    override suspend fun saveVerification(verify: Boolean?) =
        pref.saveVerification(verify)

    override fun isVerified(): Flow<Boolean?> =
        pref.isVerified()

    override suspend fun saveToken(token: String?) =
        pref.saveToken(token)

    override fun getToken(): Flow<String?> = pref.getToken()

    override suspend fun signOut() =
        pref.clearData()
}