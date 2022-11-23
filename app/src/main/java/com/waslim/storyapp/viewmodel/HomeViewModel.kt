package com.waslim.storyapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.waslim.storyapp.model.Constants
import com.waslim.storyapp.model.response.story.Story
import com.waslim.storyapp.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val homeRepository: HomeRepository) : ViewModel() {

    fun getAllStories(token: String): LiveData<PagingData<Story>> =
        homeRepository.getStoryPagination(Constants.BEARER + token).cachedIn(viewModelScope)
}