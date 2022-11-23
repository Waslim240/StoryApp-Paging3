package com.waslim.storyapp.repository

import com.waslim.storyapp.model.Result
import com.waslim.storyapp.model.response.story.Story
import com.waslim.storyapp.model.service.network.ApiService
import javax.inject.Inject

class DetailStoryRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun getDetailStory(token: String, id: String) : Result<Story> = try {
        val dataDetail = apiService.getDetailStories(token, id)
        Result.Success(dataDetail)
    } catch (e: Exception) {
        Result.Failure(e.message.toString())
    }
}