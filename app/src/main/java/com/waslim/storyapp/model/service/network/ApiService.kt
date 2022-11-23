package com.waslim.storyapp.model.service.network

import com.waslim.storyapp.model.response.login.LoginResponse
import com.waslim.storyapp.model.response.register.RegisterResponse
import com.waslim.storyapp.model.response.story.AddNewStoryResponse
import com.waslim.storyapp.model.response.story.GetAllStoryResponse
import com.waslim.storyapp.model.response.story.Story
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("register")
    suspend fun registerUser(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun loginUser(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @GET("stories")
    suspend fun getAllStories(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): GetAllStoryResponse

    @GET("stories")
    suspend fun getStoryWithLocation(
        @Header("Authorization") token: String,
        @Query("location") location : Int
    ) : GetAllStoryResponse

    @GET("stories/{id}")
    suspend fun getDetailStories(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Story

    @Multipart
    @POST("stories")
    suspend fun addStory(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody
    ): AddNewStoryResponse

    @Multipart
    @POST("stories")
    suspend fun addStoryWithLocation(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: RequestBody?,
        @Part("lon") lon: RequestBody?
    ): AddNewStoryResponse

}