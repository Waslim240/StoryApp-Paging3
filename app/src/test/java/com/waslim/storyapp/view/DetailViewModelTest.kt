package com.waslim.storyapp.view

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.waslim.storyapp.DataDummy
import com.waslim.storyapp.MainCoroutinesRule
import com.waslim.storyapp.model.Result
import com.waslim.storyapp.model.response.story.Story
import com.waslim.storyapp.repository.DetailStoryRepository
import com.waslim.storyapp.viewmodel.DetailStoryViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class DetailViewModelTest {
    private lateinit var fakeRepository: DetailStoryRepository
    private lateinit var detailStoryViewModel: DetailStoryViewModel

    private val dataDummy = DataDummy.showDataDummyStory()
    private val token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLTBDdWxlLVVPVlBmQzhxQVYiLCJpYXQiOjE2NjgxNzU5OTB9.fVSTZnmZBLauSxiVuPvqgi9B_q0_HmFDBIs__QNRKw8"
    private val id = "wh12ndh6"

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutinesRule = MainCoroutinesRule()

    @Before
    fun setup() {
        fakeRepository = mock(DetailStoryRepository::class.java)
        detailStoryViewModel = DetailStoryViewModel(fakeRepository)
    }

    @Test
    fun `Failed to get story details`() = runTest {
        val actual = MutableLiveData<Result<Story>>()
        actual.value = Result.Failure("error")

        `when`(fakeRepository.getDetailStory("", id)).thenReturn(actual.value)

        detailStoryViewModel.getDetailStory("", id )
        detailStoryViewModel.detailStories.observeForever {
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
    fun `Success to get detail story`() = runTest {
        val actual = MutableLiveData<Result<Story>>()
        actual.value = Result.Success(dataDummy)

        `when`(fakeRepository.getDetailStory(token, id)).thenReturn(actual.value)

        detailStoryViewModel.getDetailStory(token, id )
        detailStoryViewModel.detailStories.observeForever {
            when(it) {
                is Result.Success -> {
                    assertNotNull(it.data)
                    assertEquals("error", it.data)
                }
                else -> {}
            }
        }
    }
}