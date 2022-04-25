package com.ra.storyapp.ui.maps

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import com.ra.storyapp.domain.usecase.IStoryAppUseCase
import com.ra.storyapp.utils.BEARER_TOKEN

class MapsViewModel(
    private val useCase: IStoryAppUseCase
): ViewModel() {
    fun getAllStoriesWithLocation() =
        useCase.getToken().asLiveData().switchMap {
            val token = it ?: ""
            useCase.getAllStoriesWithLocation("$BEARER_TOKEN $token").asLiveData()
        }
}