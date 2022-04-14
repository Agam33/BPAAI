package com.ra.storyapp.ui.addstory

import androidx.lifecycle.*
import com.ra.storyapp.data.source.remote.network.response.FileUploadResponse
import com.ra.storyapp.domain.usecase.IStoryAppUseCase
import com.ra.storyapp.utils.BEARER_TOKEN
import com.ra.storyapp.utils.Resources
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.File

class AddStoryViewModel(
    private val useCase: IStoryAppUseCase
): ViewModel() {

    private var _fileUploadResponse = MutableLiveData<Resources<FileUploadResponse>>()
    val fileUploadResponse: LiveData<Resources<FileUploadResponse>> = _fileUploadResponse

    fun addNewStory(file: File, description: String)  {
        viewModelScope.launch {
            val token = useCase.getToken().first()
            useCase.addNewStory("$BEARER_TOKEN $token", file, description).collect {
                _fileUploadResponse.postValue(it)
            }
        }
    }
}