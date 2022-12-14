package com.waslim.storyapp.view

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import com.waslim.storyapp.*
import com.waslim.storyapp.StoryPagingTest
import com.waslim.storyapp.model.Result
import com.waslim.storyapp.model.response.story.Story
import com.waslim.storyapp.repository.HomeRepository
import com.waslim.storyapp.view.adapter.StoryPagingAdapter
import com.waslim.storyapp.viewmodel.HomeViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class HomeViewModelTest {
    private lateinit var fakeRepository: HomeRepository
    private lateinit var homeViewModel: HomeViewModel

    private val token = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLTBDdWxlLVVPVlBmQzhxQVYiLCJpYXQiOjE2NjgxNzU5OTB9.fVSTZnmZBLauSxiVuPvqgi9B_q0_HmFDBIs__QNRKw8"
    private val dummyStory = DataDummy.showDataDummyListStories()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutinesRule = MainCoroutinesRule()

    @Before
    fun setup() {
        fakeRepository = mock(HomeRepository::class.java)
        homeViewModel = HomeViewModel(fakeRepository)
    }

    @Test
    fun `Get story with pagination successfully`() = runTest {
        val data: PagingData<Story> = StoryPagingTest.snapshot(dummyStory)
        val expected = MutableLiveData<PagingData<Story>>()
        expected.value = data

        `when`(fakeRepository.getStoryPagination(token)).thenReturn(expected)

        val actual : PagingData<Story> = homeViewModel.getAllStories(token).getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryPagingAdapter.DIFF_CALLBACK,
            updateCallback = ListUpdateCallback.noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )

        differ.submitData(actual)

        verify(fakeRepository).getStoryPagination(token)
        assertNotNull(differ.snapshot())
        assertEquals(dummyStory, differ.snapshot())
        assertEquals(dummyStory.size, differ.snapshot().size)
        assertEquals(dummyStory[0].id, differ.snapshot()[0]?.id)
    }
}