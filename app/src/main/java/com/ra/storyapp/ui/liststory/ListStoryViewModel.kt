package com.ra.storyapp.ui.liststory

import androidx.lifecycle.*
import com.ra.storyapp.domain.model.Story
import com.ra.storyapp.domain.usecase.IStoryAppUseCase
import com.ra.storyapp.utils.BEARER_TOKEN
import com.ra.storyapp.utils.Resources
import kotlinx.coroutines.launch

class ListStoryViewModel(
    private val useCase: IStoryAppUseCase
): ViewModel() {
    
    fun getAllStory(): LiveData<Resources<List<Story>>> =
        useCase.getToken().asLiveData().switchMap {
        useCase.getAllStories("$BEARER_TOKEN ${it ?: ""}").asLiveData()
    }

    fun signOut() = viewModelScope.launch {
        useCase.signOut()
    }
}

