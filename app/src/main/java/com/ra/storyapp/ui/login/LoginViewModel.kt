package com.ra.storyapp.ui.login

import androidx.lifecycle.*
import com.ra.storyapp.domain.model.LoginResult
import com.ra.storyapp.domain.usecase.IStoryAppUseCase
import com.ra.storyapp.utils.Event
import com.ra.storyapp.utils.Resources
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


class LoginViewModel(
    private val useCase: IStoryAppUseCase
): ViewModel() {

    private var _isVerified = MutableLiveData<Event<Boolean>>()
    val isVerified: LiveData<Event<Boolean>> = _isVerified

    fun checkVerification() {
        viewModelScope.launch {
            val state = useCase.isVerified().first() ?: false
            _isVerified.value = (Event(state))
        }
    }

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