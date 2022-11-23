package com.waslim.storyapp.view

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import com.waslim.storyapp.MainCoroutinesRule
import com.waslim.storyapp.getOrAwaitValue
import com.waslim.storyapp.repository.UserTokenRepository
import com.waslim.storyapp.viewmodel.UserTokenViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.robolectric.RobolectricTestRunner

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
class UserTokenViewModelTest {
    private lateinit var fakeRepository: UserTokenRepository
    private lateinit var userTokenViewModel: UserTokenViewModel

    private val token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLTBDdWxlLVVPVlBmQzhxQVYiLCJpYXQiOjE2NjgxNzU5OTB9.fVSTZnmZBLauSxiVuPvqgi9B_q0_HmFDBIs__QNRKw8"

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainCoroutinesRule()

    @Before
    fun setup() {
        fakeRepository = Mockito.mock(UserTokenRepository::class.java)
        userTokenViewModel = UserTokenViewModel(fakeRepository)
    }

    @Test
    fun `set Token Success`(): Unit = runTest {
        userTokenViewModel.setToken(token)
        verify(fakeRepository).setToken(token)
    }

    @Test
    fun `get Token Success`() {
        val actual = MutableLiveData<String>()
        actual.value = token

        `when`(fakeRepository.getToken()).thenReturn(actual.asFlow())
        userTokenViewModel.getToken().getOrAwaitValue()

        verify(fakeRepository).getToken()

        assertEquals(token, actual.value)
    }

    @Test
    fun `remove Token Success`(): Unit = runTest {
        userTokenViewModel.removeToken()
        verify(fakeRepository).removeToken()
    }
}