package com.waslim.storyapp.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.waslim.storyapp.MainCoroutinesRule
import com.waslim.storyapp.Module
import com.waslim.storyapp.model.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class LoginRepositoryTest {
    private lateinit var loginRepository: LoginRepository

    private val email = "waslim@gmail.com"
    private val password = "Waslim123"

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainCoroutinesRule()

    @Before
    fun setup() {
        val apiService = Module.providesRetrofit()
        loginRepository = LoginRepository(apiService)
    }

    @Test
    fun `Login with wrong email or password`() = runTest {
        val expected = loginRepository.loginUser(email, "Waslim12")
        assertTrue(expected is Result.Failure)
    }

    @Test
    fun `Login with an empty email or password`() = runTest {
        val expected = loginRepository.loginUser("", "")
        assertTrue(expected is Result.Failure)
    }

    @Test
    fun `Login successfully with correct email and password`() = runTest {
        val expected = loginRepository.loginUser(email, password)
        assertTrue(expected is Result.Success)
    }
}