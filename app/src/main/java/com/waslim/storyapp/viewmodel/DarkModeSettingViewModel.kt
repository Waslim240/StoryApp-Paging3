package com.waslim.storyapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.waslim.storyapp.repository.DarkModeSettingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DarkModeSettingViewModel @Inject constructor(private val darkModeSettingRepository: DarkModeSettingRepository) : ViewModel() {

    fun getThemeSetting(): LiveData<Boolean> =
        darkModeSettingRepository.getThemeSetting().asLiveData()

    fun setThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            darkModeSettingRepository.setThemeSetting(isDarkModeActive)
        }
    }
}