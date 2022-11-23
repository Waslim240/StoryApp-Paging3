package com.waslim.storyapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waslim.storyapp.model.Result
import com.waslim.storyapp.model.response.login.LoginResponse
import com.waslim.storyapp.repository.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val loginRepository: LoginRepository) : ViewModel() {
    private val _loginUser = MutableLiveData<Result<LoginResponse>>()
    val loginUser: LiveData<Result<LoginResponse>> = _loginUser

    fun loginUser(email: String, password: String) {
        _loginUser.value = Result.Loading
        viewModelScope.launch {
            _loginUser.value = loginRepository.loginUser(email, password)
        }
    }
}