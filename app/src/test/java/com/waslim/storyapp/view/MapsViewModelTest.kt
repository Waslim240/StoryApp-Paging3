package com.waslim.storyapp.view

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.waslim.storyapp.DataDummy
import com.waslim.storyapp.MainCoroutinesRule
import com.waslim.storyapp.model.Result
import com.waslim.storyapp.model.response.story.GetAllStoryResponse
import com.waslim.storyapp.repository.MapsRepository
import com.waslim.storyapp.viewmodel.MapsViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class MapsViewModelTest {
    private lateinit var fakeRepository: MapsRepository
    private lateinit var mapsViewModel: MapsViewModel

    private val dummyResponse = DataDummy.showDataDummyStoryResponse()

    private val token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLTBDdWxlLVVPVlBmQzhxQVYiLCJpYXQiOjE2NjgxNzU5OTB9.fVSTZnmZBLauSxiVuPvqgi9B_q0_HmFDBIs__QNRKw8"
    private val location = 1

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutinesRule = MainCoroutinesRule()

    @Before
    fun setup() {
        fakeRepository = Mockito.mock(MapsRepository::class.java)
        mapsViewModel = MapsViewModel(fakeRepository)
    }

    @Test
    fun `Failed to get story with location`() = runTest {
        val actual = MutableLiveData<Result<GetAllStoryResponse>>()
        actual.value = Result.Failure("error")

        `when`(fakeRepository.getStoryWithLocation("token_salah", location)).thenReturn(actual.value)

        mapsViewModel.getStoryWithLocation("token_salah", location)
        mapsViewModel.maps.observeForever {
            when(it) {
                is Result.Failure -> {
                    assertNotNull(it.message)
                    assertEquals("error", it.message)
                }
                else -> {}
            }
        }
    }

    @Test
    fun `Success to get story with location`() = runTest {
        val actual = MutableLiveData<Result<GetAllStoryResponse>>()
        actual.value = Result.Success(dummyResponse)

        `when`(fakeRepository.getStoryWithLocation(token, location)).thenReturn(actual.value)

        mapsViewModel.getStoryWithLocation(token, location )
        mapsViewModel.maps.observeForever {
            when(it) {
                is Result.Success -> {
                    assertNotNull(it.data)
                    assertEquals(dummyResponse, it.data)
                }
                else -> {}
            }
        }
    }
}