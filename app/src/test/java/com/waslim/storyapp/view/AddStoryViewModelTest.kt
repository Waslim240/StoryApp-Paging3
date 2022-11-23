package com.waslim.storyapp.view

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.waslim.storyapp.DataDummy
import com.waslim.storyapp.MainCoroutinesRule
import com.waslim.storyapp.model.Result
import com.waslim.storyapp.model.response.story.AddNewStoryResponse
import com.waslim.storyapp.repository.AddStoryRepository
import com.waslim.storyapp.viewmodel.AddStoryViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
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
class AddStoryViewModelTest {
    private lateinit var addStoryViewModel: AddStoryViewModel
    private lateinit var fakeRepository: AddStoryRepository

    private val dummyResponse = DataDummy.showDataDummyAddStoryResponse()

    private val token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLTBDdWxlLVVPVlBmQzhxQVYiLCJpYXQiOjE2NjgxNzU5OTB9.fVSTZnmZBLauSxiVuPvqgi9B_q0_HmFDBIs__QNRKw8"
    private val description: RequestBody = "Ini Deskripsi".toRequestBody()
    private val file = MultipartBody.Part.create("file".toRequestBody())
    private val location = LatLng(-6.159839, 107.489743)
    private val lat: RequestBody = location.latitude.toString().toRequestBody()
    private val lon: RequestBody = location.longitude.toString().toRequestBody()

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutinesRule = MainCoroutinesRule()

    @Before
    fun setup() {
        fakeRepository = Mockito.mock(AddStoryRepository::class.java)
        addStoryViewModel = AddStoryViewModel(fakeRepository)
    }

    @Test
    fun `Success upload story`() = runTest {
        val actual = MutableLiveData<Result<AddNewStoryResponse>>()
        actual.value = Result.Success(dummyResponse)

        `when`(
            fakeRepository.addNewStory(token, file, description )
        ).thenReturn(actual.value)

        addStoryViewModel.addNewStory(token, file, description)
        addStoryViewModel.uploadStory.observeForever {
            when(it) {
                is Result.Success -> {
                    assertNotNull(it.data)
                    assertEquals(dummyResponse, it.data)
                }
                else -> {}
            }
        }
    }

    @Test
    fun `Success upload story with location`() = runTest {
        val actual = MutableLiveData<Result<AddNewStoryResponse>>()
        actual.value = Result.Success(dummyResponse)

        `when`(
            fakeRepository.addNewStoryWithLocation(token, file, description, lat, lon)
        ).thenReturn(actual.value)

        addStoryViewModel.addNewStoryWithLocation(token, file, description, lat, lon)
        addStoryViewModel.uploadStory.observeForever {
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