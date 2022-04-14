package com.ra.storyapp.data.source.remote.network

sealed class ApiResponse<out RESPONSE> {
    data class Success<out T>(val data: T): ApiResponse<T>()
    data class Error(val errorMsg: String): ApiResponse<Nothing>()
    object Empty: ApiResponse<Nothing>()
}
