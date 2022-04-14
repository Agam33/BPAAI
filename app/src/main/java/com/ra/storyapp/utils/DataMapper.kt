package com.ra.storyapp.utils

import com.ra.storyapp.data.source.remote.network.response.LoginResultResponse
import com.ra.storyapp.data.source.remote.network.response.StoriesResponse
import com.ra.storyapp.domain.model.LoginResult
import com.ra.storyapp.domain.model.Story

object DataMapper {

    fun storyResponseToModel(response: StoriesResponse): Story =
         Story(
            response.id,
            response.name,
            response.description,
            response.photoUrl,
            response.createdAt,
        )

    fun loginResultResponseToModel(response: LoginResultResponse): LoginResult =
        LoginResult(
            response.userId,
            response.name,
            response.token
        )

}