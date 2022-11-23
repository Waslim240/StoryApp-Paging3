package com.waslim.storyapp.repository

import com.waslim.storyapp.model.Result
import com.waslim.storyapp.model.response.register.RegisterResponse
import com.waslim.storyapp.model.service.network.ApiService
import javax.inject.Inject

class RegisterRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun registerUser(name: String, email: String, password: String) : Result<RegisterResponse> =
        try {
            val dataRegister = apiService.registerUser(name, email, password)
            Result.Success(dataRegister)
        } catch (e: Exception) {
            Result.Failure(e.message.toString())
        }
}