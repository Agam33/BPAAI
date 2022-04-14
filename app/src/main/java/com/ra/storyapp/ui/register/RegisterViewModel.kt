package com.ra.storyapp.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ra.storyapp.data.source.remote.network.response.RegisterResponse
import com.ra.storyapp.domain.usecase.IStoryAppUseCase
import com.ra.storyapp.utils.Resources
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val useCase: IStoryAppUseCase
): ViewModel() {

    private var _registerResult = MutableLiveData<Resources<RegisterResponse>>()
    val registerResult: LiveData<Resources<RegisterResponse>> = _registerResult

    fun register(
        name: String,
        email: String,
        password: String
    ) {
        viewModelScope.launch {
            useCase.register(name, email, password).collect {
                _registerResult.postValue(it)
            }
        }
    }
}