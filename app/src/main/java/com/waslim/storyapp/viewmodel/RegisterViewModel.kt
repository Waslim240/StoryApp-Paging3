package com.waslim.storyapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waslim.storyapp.model.Result
import com.waslim.storyapp.model.response.register.RegisterResponse
import com.waslim.storyapp.repository.RegisterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val registerRepository: RegisterRepository) : ViewModel() {
    private val _register = MutableLiveData<Result<RegisterResponse>>()
    val register: LiveData<Result<RegisterResponse>> = _register

    fun registerUser(name: String, email: String, password: String) {
        _register.value = Result.Loading
        viewModelScope.launch {
            _register.value = registerRepository.registerUser(name, email, password)
        }
    }
}