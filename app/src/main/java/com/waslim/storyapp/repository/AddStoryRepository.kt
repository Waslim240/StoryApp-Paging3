package com.waslim.storyapp.repository

import com.waslim.storyapp.model.Result
import com.waslim.storyapp.model.response.story.AddNewStoryResponse
import com.waslim.storyapp.model.service.network.ApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class AddStoryRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun addNewStory(token: String, imageFile: MultipartBody.Part, description: RequestBody) : Result<AddNewStoryResponse> =
        try {
            val dataAddNewStory = apiService.addStory(token, imageFile, description)
            Result.Success(dataAddNewStory)
        } catch (e: Exception) {
            Result.Failure(e.message.toString())
        }

    suspend fun addNewStoryWithLocation(token: String, imageFile: MultipartBody.Part, description: RequestBody, lat: RequestBody, lon: RequestBody) : Result<AddNewStoryResponse> =
        try {
            val dataAddNewStoryWithLocation = apiService.addStoryWithLocation(token, imageFile, description, lat, lon)
            Result.Success(dataAddNewStoryWithLocation)
        } catch (e: Exception) {
            Result.Failure(e.message.toString())
        }
}