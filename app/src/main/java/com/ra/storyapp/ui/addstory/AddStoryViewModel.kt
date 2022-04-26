package com.ra.storyapp.ui.addstory

import android.location.Location
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

    fun addNewStory(
        file: File,
        description: String,
        latitude: Float?,
        longitude: Float?
    )  {
        viewModelScope.launch {
            val token = useCase.getToken().first()
            useCase.addNewStory(
                "$BEARER_TOKEN $token",
                file,
                description,
                latitude,
                longitude
            ).collect {
                _fileUploadResponse.postValue(it)
            }
        }
    }

    private var _getCurrentLocation = MutableLiveData<Location>()
    val getCurrentLocation: LiveData<Location> = _getCurrentLocation

    fun setLocation(location: Location) {
        _getCurrentLocation.postValue(location)
    }
}