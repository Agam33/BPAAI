package com.ra.storyapp.data.source

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.ra.storyapp.data.source.local.database.StoryDatabase
import com.ra.storyapp.data.source.local.database.entity.RemoteKeys
import com.ra.storyapp.data.source.local.database.entity.StoryEntity
import com.ra.storyapp.data.source.remote.network.ApiService
import com.ra.storyapp.utils.BEARER_TOKEN
import com.ra.storyapp.utils.DataMapper
import com.ra.storyapp.utils.preferences.IUserPreferencesStore
import com.ra.storyapp.utils.preferences.UserPreferencesStore
import kotlinx.coroutines.flow.first
import org.koin.java.KoinJavaComponent.inject

@OptIn(ExperimentalPagingApi::class)
class StoryRemoteMediator: RemoteMediator<Int, StoryEntity>() {

    private val apiService: ApiService by inject(ApiService::class.java)
    private val database: StoryDatabase by inject(StoryDatabase::class.java)
    private val userPreferencesStore: UserPreferencesStore by inject(IUserPreferencesStore::class.java)

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override suspend fun initialize(): InitializeAction =
        InitializeAction.LAUNCH_INITIAL_REFRESH

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, StoryEntity>
    ): MediatorResult {
        val pageSize = state.config.pageSize
        val page = when(loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: INITIAL_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }

        try {
            val token = userPreferencesStore.getToken().first() ?: ""
            val responseData = apiService.getAllStories(
               "$BEARER_TOKEN $token",
                page,
                pageSize,
            ).listStory

            val endOfPaginationReached = responseData.isEmpty()

            val data = responseData.map {
                DataMapper.storyResponseToEntity(it)
            }

            database.withTransaction {
                if(loadType == LoadType.REFRESH) {
                    database.storyDao().deleteStories()
                    database.remoteKeysDao().deleteRemoteKeys()
                }
                val prevKey = if(page == INITIAL_PAGE_INDEX) null else page - 1
                val nextKey = if(endOfPaginationReached) null else page + 1
                val keys = responseData.map {
                    RemoteKeys(id = it.id, prevKey = prevKey, nextKey = nextKey)
                }
                database.remoteKeysDao().insertAll(keys)
                database.storyDao().insertStories(data)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, StoryEntity>): RemoteKeys? =
        state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { data ->
            database.remoteKeysDao().getRemoteKeysId(data.id)
        }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, StoryEntity>): RemoteKeys? =
        state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { data ->
            database.remoteKeysDao().getRemoteKeysId(data.id)
        }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, StoryEntity>): RemoteKeys? =
        state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let {  id ->
                database.remoteKeysDao().getRemoteKeysId(id)
            }
        }
}