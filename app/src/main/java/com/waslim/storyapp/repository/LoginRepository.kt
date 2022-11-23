package com.waslim.storyapp.repository

import com.waslim.storyapp.model.Result
import com.waslim.storyapp.model.response.login.LoginResponse
import com.waslim.storyapp.model.service.network.ApiService
import javax.inject.Inject

class LoginRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun loginUser(email: String, password: String) : Result<LoginResponse> = try {
        val dataLogin = apiService.loginUser(email, password)
        Result.Success(dataLogin)
    } catch (e: Exception) {
        Result.Failure(e.message.toString())
    }
}