package com.waslim.storyapp.view

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.waslim.storyapp.DataDummy
import com.waslim.storyapp.MainCoroutinesRule
import com.waslim.storyapp.getOrAwaitValue
import com.waslim.storyapp.model.Result
import com.waslim.storyapp.model.response.register.RegisterResponse
import com.waslim.storyapp.repository.RegisterRepository
import com.waslim.storyapp.viewmodel.RegisterViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.robolectric.RobolectricTestRunner

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
class RegisterViewModelTest {
    private lateinit var fakeRepository: RegisterRepository
    private lateinit var registerViewModel: RegisterViewModel

    private val dummyResponse = DataDummy.showDataDummyRegisterResponse()

    private val name = "Lim"
    private val email = "waslim@gmail.com"
    private val password = "Waslim123"

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainCoroutinesRule()

    @Before
    fun setup() {
        fakeRepository = mock(RegisterRepository::class.java)
        registerViewModel = RegisterViewModel(fakeRepository)
    }

    @Test
    fun `Register successful`() = runTest {
        val expected = MutableLiveData<Result<RegisterResponse>>()
        expected.value = Result.Success(dummyResponse)

        `when`(
            fakeRepository.registerUser(name, "waslim2@gmail.com", password)
        ).thenReturn(expected.value)

        registerViewModel.registerUser(name, "waslim2@gmail.com", password)

        val actual = registerViewModel.register.getOrAwaitValue()

        verify(fakeRepository).registerUser(name, "waslim2@gmail.com", password)
        assertNotNull(actual)
        assertTrue(actual is Result.Success)
        assertEquals(dummyResponse, (actual as Result.Success).data)
    }

    @Test
    fun `Register failed`() = runTest {
        val expected = MutableLiveData<Result<RegisterResponse>>()
        expected.value = Result.Failure("error")

        `when`(
            fakeRepository.registerUser(name, email, password)
        ).thenReturn(expected.value)

        registerViewModel.registerUser(name, email, password)

        val actual = registerViewModel.register.getOrAwaitValue()

        verify(fakeRepository).registerUser(name, email, password)
        assertNotNull(actual)
        assertTrue(actual is Result.Failure)
        assertEquals("error", (actual as Result.Failure).message)
    }

}