package com.waslim.storyapp.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.waslim.storyapp.DataDummy
import com.waslim.storyapp.MainCoroutinesRule
import com.waslim.storyapp.model.Result
import com.waslim.storyapp.Module
import com.waslim.storyapp.model.response.login.LoginResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class LoginRepositoryTest {
    private lateinit var loginRepository: LoginRepository

    private val dummyLoginResponse = DataDummy.showDataDummyLoginResponse()

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
        val actualLogin = loginRepository.loginUser(email, "Waslim12")
        assertTrue(actualLogin is Result.Failure)
    }

    @Test
    fun `Login with an empty email or password`() = runTest {
        val actualLogin = loginRepository.loginUser("", "")
        assertTrue(actualLogin is Result.Failure)
    }

    @Test
    fun `Login successfully with correct email and password`() = runTest {
        val actualLogin = loginRepository.loginUser(email, password)
        assertTrue(actualLogin is Result.Success)
    }

    @Test
    fun test() = runTest {
        val expected = MutableLiveData<Result<LoginResponse>>()
        expected.value = Result.Success(dummyLoginResponse)

        `when`(loginRepository.loginUser(email, password)).thenReturn(expected.value)

        loginRepository.loginUser(email, password)

    }
}