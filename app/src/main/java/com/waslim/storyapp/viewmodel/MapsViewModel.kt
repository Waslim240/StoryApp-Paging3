package com.waslim.storyapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waslim.storyapp.model.Result
import com.waslim.storyapp.model.response.story.GetAllStoryResponse
import com.waslim.storyapp.repository.MapsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor(private val mapsRepository: MapsRepository) : ViewModel() {
    private val _maps = MutableLiveData<Result<GetAllStoryResponse>>()
    val maps: LiveData<Result<GetAllStoryResponse>> = _maps

    fun getStoryWithLocation(token: String, location: Int) {
        _maps.value = Result.Loading
        viewModelScope.launch {
            _maps.value = mapsRepository.getStoryWithLocation(token, location)
        }
    }
}