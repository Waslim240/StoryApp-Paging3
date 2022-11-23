package com.waslim.storyapp.repository

import com.waslim.storyapp.model.Result
import com.waslim.storyapp.model.response.story.GetAllStoryResponse
import com.waslim.storyapp.model.service.network.ApiService
import javax.inject.Inject

class MapsRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun getStoryWithLocation(token: String, location: Int) : Result<GetAllStoryResponse> =
        try {
            val storyLocation = apiService.getStoryWithLocation(token, location)
            Result.Success(storyLocation)
        } catch (e: Exception) {
            Result.Failure(e.message.toString())
        }
}