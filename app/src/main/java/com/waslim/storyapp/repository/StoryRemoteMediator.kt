package com.waslim.storyapp.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.waslim.storyapp.model.Constants
import com.waslim.storyapp.model.local.RemoteKeys
import com.waslim.storyapp.model.local.StoryDatabase
import com.waslim.storyapp.model.response.story.GetAllStoryResponse
import com.waslim.storyapp.model.response.story.Story
import com.waslim.storyapp.model.service.network.ApiService
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class StoryRemoteMediator @Inject constructor(private val storyDatabase : StoryDatabase, private val apiService: ApiService, private val token: String) : RemoteMediator<Int, Story>() {

    override suspend fun initialize(): InitializeAction = InitializeAction.LAUNCH_INITIAL_REFRESH

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Story>): MediatorResult {
        val page = when(loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.next?.minus(1) ?: Constants.INITIAL_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.previous ?: return MediatorResult.Success(remoteKeys != null)
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.next ?: return MediatorResult.Success(remoteKeys != null)
                nextKey
            }
        }

        try {
            val dataStory : GetAllStoryResponse = apiService.getAllStories(token, page, state.config.pageSize)
            val endOfPaginationReached = dataStory.listStory?.isEmpty()

            storyDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    storyDatabase.remoteKeysDao().deleteRemoteKeys()
                    storyDatabase.storyDao().deleteAll()
                }

                val prevKey = when (page) {
                    1 -> null
                    else -> page - 1
                }

                val nextKey = when (endOfPaginationReached) {
                    true -> null
                    else -> page + 1
                }

                val keys = dataStory.listStory?.map { RemoteKeys(it.id, prevKey, nextKey) }
                storyDatabase.remoteKeysDao().insertAll(keys)

                val localStories = arrayListOf<Story>()
                dataStory.listStory?.forEach { localStories.add(Constants.mapStory(it)) }

                storyDatabase.storyDao().insertStory(localStories as List<Story>)
            }
            return MediatorResult.Success(endOfPaginationReached!!)
        } catch (exception: Exception) {
            InitializeAction.LAUNCH_INITIAL_REFRESH
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Story>): RemoteKeys? =
        state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { story ->
            storyDatabase.remoteKeysDao().getRemoteKeysId(story.id)
        }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Story>): RemoteKeys? =
        state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { localStory ->
            storyDatabase.remoteKeysDao().getRemoteKeysId(localStory.id)
        }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, Story>): RemoteKeys? =
        state.anchorPosition?.let {
            state.closestItemToPosition(it)?.id?.let { id ->
                storyDatabase.remoteKeysDao().getRemoteKeysId(id)
            }
        }
}