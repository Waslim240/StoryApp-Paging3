package com.waslim.storyapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waslim.storyapp.model.Constants
import com.waslim.storyapp.model.Result
import com.waslim.storyapp.model.response.story.Story
import com.waslim.storyapp.repository.DetailStoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailStoryViewModel @Inject constructor(private val detailStoryRepository: DetailStoryRepository) : ViewModel() {

    private val _detailStories = MutableLiveData<Result<Story?>>()
    val detailStories: LiveData<Result<Story?>> = _detailStories

    fun getDetailStory(token: String, id: String) {
        _detailStories.value = Result.Loading
        viewModelScope.launch {
            _detailStories.value = detailStoryRepository.getDetailStory(Constants.BEARER + token, id)
        }
    }

}