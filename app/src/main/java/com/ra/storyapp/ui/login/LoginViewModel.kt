package com.ra.storyapp.ui.login

import androidx.lifecycle.*
import com.ra.storyapp.domain.model.LoginResult
import com.ra.storyapp.domain.usecase.IStoryAppUseCase
import com.ra.storyapp.utils.Event
import com.ra.storyapp.utils.Resources
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class LoginViewModel(
    private val useCase: IStoryAppUseCase
): ViewModel() {

    private val _isVerified: MutableLiveData<Event<Boolean>> by lazy {
        MutableLiveData<Event<Boolean>>().apply {
            viewModelScope.launch {
              useCase.isVerified().first()?.let { postValue(Event(it)) }
                  ?: postValue(Event(false))
            }
        }
    }
    val isVerified: LiveData<Event<Boolean>> = _isVerified

    private var _loginResult = MutableLiveData<Resources<LoginResult>>()
    val loginResult: LiveData<Resources<LoginResult>> = _loginResult

    fun loginWithEmailAndPassword(email: String, password: String) {
        viewModelScope.launch {
            useCase.login(email, password).collect {
                _loginResult.postValue(it)
            }
        }
    }

    fun saveVerification(state: Boolean?) = viewModelScope.launch {
        useCase.saveVerification(state)
    }

    fun saveToken(token: String?) = viewModelScope.launch {
        useCase.saveToken(token)
    }
}