package com.waslim.storyapp.repository

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.waslim.storyapp.model.PagingDataSource
import com.waslim.storyapp.model.response.story.Story
import javax.inject.Inject

class HomeRepository @Inject constructor(private val pagingDataSource: PagingDataSource) {
    fun getStoryPagination(token: String): LiveData<PagingData<Story>> =
        pagingDataSource.getAllStory(token)
}