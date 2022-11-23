package com.waslim.storyapp.model

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.waslim.storyapp.model.local.StoryDatabase
import com.waslim.storyapp.model.response.story.Story
import com.waslim.storyapp.model.service.network.ApiService
import com.waslim.storyapp.repository.StoryRemoteMediator
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class PagingDataSource @Inject constructor(private val apiService: ApiService, private val storyDatabase: StoryDatabase){
    fun getAllStory(token: String) : LiveData<PagingData<Story>> = Pager(
        config = PagingConfig(Constants.PAGE_SIZE),
        remoteMediator = StoryRemoteMediator(storyDatabase, apiService, token),
        pagingSourceFactory = storyDatabase.storyDao()::getAllStory
    ).liveData
}