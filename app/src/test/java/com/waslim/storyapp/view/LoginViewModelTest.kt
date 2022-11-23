package com.waslim.storyapp.view

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.waslim.storyapp.DataDummy
import com.waslim.storyapp.MainCoroutinesRule
import com.waslim.storyapp.model.Result
import com.waslim.storyapp.model.response.login.LoginResponse
import com.waslim.storyapp.repository.LoginRepository
import com.waslim.storyapp.viewmodel.LoginViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class LoginViewModelTest {
    private lateinit var fakeRepository: LoginRepository
    private lateinit var loginViewModel: LoginViewModel

    private val dummyLoginResponse = DataDummy.showDataDummyLoginResponse()
    private val email = "waslim@gmail.com"
    private val password = "Waslim123"

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutinesRule = MainCoroutinesRule()

    @Before
    fun setup() {
        fakeRepository = mock(LoginRepository::class.java)
        loginViewModel = LoginViewModel(fakeRepository)
    }

    @Test
    fun `Login success`() = runTest {
        val expected = MutableLiveData<Result<LoginResponse>>()
        expected.value = Result.Success(dummyLoginResponse)

        `when`(fakeRepository.loginUser(email, password)).thenReturn(expected.value)

        loginViewModel.loginUser(email, password)
        loginViewModel.loginUser.observeForever{
            when(it) {
                is Result.Success -> {
                    assertNotNull(it.data)
                    assertEquals(dummyLoginResponse, it.data)
                }
                else -> {}
            }
        }
    }

    @Test
    fun `Login with wrong email or password`() = runTest {
        val actual = MutableLiveData<Result<LoginResponse>>()
        actual.value = Result.Failure("error")

        `when`(
            fakeRepository.loginUser(email, "salah")).thenReturn(actual.value)

        loginViewModel.loginUser(email, "salah")
        loginViewModel.loginUser.observeForever {
            when(it){
                is Result.Failure -> {
                    assertNotNull(it.message)
                    assertEquals("error", it.message)
                }
                else -> {}
            }
        }
    }

    @Test
    fun `Login with an empty email or password`() = runTest {
        val expected = MutableLiveData<Result<LoginResponse>>()
        expected.value = Result.Failure("error")

        `when`(fakeRepository.loginUser("", "")).thenReturn(expected.value)

        loginViewModel.loginUser("", "")
        loginViewModel.loginUser.observeForever {
            when(it){
                is Result.Failure -> {
                    assertNotNull(it.message)
                    assertEquals("error", it.message)
                }
                else -> {}
            }
        }
    }
}