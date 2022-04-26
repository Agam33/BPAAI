package com.ra.storyapp.ui.maps

import androidx.lifecycle.*
import com.ra.storyapp.R
import com.ra.storyapp.domain.usecase.IStoryAppUseCase
import com.ra.storyapp.utils.BEARER_TOKEN
import com.ra.storyapp.utils.MapStyleOption
class MapsViewModel(
    private val useCase: IStoryAppUseCase
): ViewModel() {
    fun getAllStoriesWithLocation() =
        useCase.getToken().asLiveData().switchMap {
            val token = it ?: ""
            useCase.getAllStoriesWithLocation("$BEARER_TOKEN $token").asLiveData()
        }

    private var _getMapStyle: MutableLiveData<Int> = MutableLiveData<Int>()
    val getMapStyle: LiveData<Int> = _getMapStyle

    fun setMapStyle(style: MapStyleOption) {
        when(style) {
            MapStyleOption.RETRO -> _getMapStyle.postValue(R.raw.retro_map_style)
            MapStyleOption.DARK -> _getMapStyle.postValue(R.raw.dark_map_style)
            MapStyleOption.SILVER -> _getMapStyle.postValue(R.raw.silver_maps_style)
            MapStyleOption.STANDARD -> _getMapStyle.postValue(R.raw.standard_map_style)
        }
    }
}

