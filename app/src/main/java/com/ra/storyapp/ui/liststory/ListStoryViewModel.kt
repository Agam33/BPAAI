package com.ra.storyapp.ui.liststory

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.ra.storyapp.domain.model.Story
import com.ra.storyapp.domain.usecase.IStoryAppUseCase
import com.ra.storyapp.utils.BEARER_TOKEN
import com.ra.storyapp.utils.DataMapper
import com.ra.storyapp.utils.Resources
import kotlinx.coroutines.launch

class ListStoryViewModel(
    private val useCase: IStoryAppUseCase
): ViewModel() {
    
    fun getAllStory(): LiveData<PagingData<Story>> =
        useCase.getAllStories().map {
            it.map { entities ->
                DataMapper.storyEntityToModel(entities)
            }
        }.cachedIn(viewModelScope)

    fun signOut() = viewModelScope.launch {
        useCase.signOut()
    }
}

