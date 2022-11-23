package com.waslim.storyapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waslim.storyapp.model.Constants
import com.waslim.storyapp.model.Result
import com.waslim.storyapp.model.response.story.AddNewStoryResponse
import com.waslim.storyapp.repository.AddStoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class AddStoryViewModel @Inject constructor(private val addStoryRepository: AddStoryRepository) : ViewModel() {
    private val _uploadStory = MutableLiveData<Result<AddNewStoryResponse>>()
    val uploadStory : LiveData<Result<AddNewStoryResponse>> = _uploadStory

    fun addNewStoryWithLocation(token: String, imageFile: MultipartBody.Part, description: RequestBody, lat: RequestBody, lon: RequestBody) {
        _uploadStory.value = Result.Loading
        viewModelScope.launch {
            _uploadStory.value = addStoryRepository.addNewStoryWithLocation(Constants.BEARER + token, imageFile, description, lat, lon)
        }
    }

    fun addNewStory(token: String, imageFile: MultipartBody.Part, description: RequestBody) {
        _uploadStory.value = Result.Loading
        viewModelScope.launch {
            _uploadStory.value = addStoryRepository.addNewStory(Constants.BEARER + token, imageFile, description)
        }
    }
}