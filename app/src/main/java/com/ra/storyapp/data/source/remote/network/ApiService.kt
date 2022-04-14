package com.ra.storyapp.data.source.remote.network

import com.ra.storyapp.data.source.remote.network.response.*
import com.ra.storyapp.utils.ADD_NEW_STORY_URL
import com.ra.storyapp.utils.GET_ALL_STORY_URL
import com.ra.storyapp.utils.LOGIN_URL
import com.ra.storyapp.utils.REGISTER_URL
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {

    @GET(GET_ALL_STORY_URL)
    suspend fun getAllStories(
        @Header("Authorization") authorization: String
    ): StoryResponse

    @FormUrlEncoded
    @POST(REGISTER_URL)
    suspend fun registerAccount(
       @Field("name") name: String,
       @Field("email") email: String,
       @Field("password") password: String
    ): RegisterResponse

    @FormUrlEncoded
    @POST(LOGIN_URL)
    suspend fun login(
       @Field("email") email: String,
       @Field("password") password: String,
    ): LoginResponse

    @Multipart
    @POST(ADD_NEW_STORY_URL)
    suspend fun addNewStory(
        @Header("Authorization") authorization: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody
    ): FileUploadResponse

}