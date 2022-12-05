package com.waslim.storyapp.view

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import com.waslim.storyapp.MainCoroutinesRule
import com.waslim.storyapp.getOrAwaitValue
import com.waslim.storyapp.repository.DarkModeSettingRepository
import com.waslim.storyapp.viewmodel.DarkModeSettingViewModel
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
class DarkModeSettingViewModelTest {
    private lateinit var fakeRepository: DarkModeSettingRepository
    private lateinit var darkModeViewModel: DarkModeSettingViewModel

    private val theme = true

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainCoroutinesRule()

    @Before
    fun setup() {
        fakeRepository = Mockito.mock(DarkModeSettingRepository::class.java)
        darkModeViewModel = DarkModeSettingViewModel(fakeRepository)
    }

    @Test
    fun `set Theme Setting Success`(): Unit = runTest {
        darkModeViewModel.setThemeSetting(theme)
        verify(fakeRepository).setThemeSetting(theme)
    }

    @Test
    fun `get Theme Setting Success`() {
        val expected = MutableLiveData<Boolean>()
        expected.value = theme

        `when`(fakeRepository.getThemeSetting()).thenReturn(expected.asFlow())

        val actual = darkModeViewModel.getThemeSetting().getOrAwaitValue()

        verify(fakeRepository).getThemeSetting()

        assertNotNull(actual)
        assertEquals(theme, actual)
    }

}