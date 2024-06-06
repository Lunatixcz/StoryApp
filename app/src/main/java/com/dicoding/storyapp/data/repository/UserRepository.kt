package com.dicoding.storyapp.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.dicoding.storyapp.data.api.ApiService
import com.dicoding.storyapp.data.database.StoryDatabase
import com.dicoding.storyapp.data.pref.UserPreference
import com.dicoding.storyapp.data.response.DetailStoryResponse
import com.dicoding.storyapp.data.response.ListStoryItem
import com.dicoding.storyapp.data.response.StoryResponse
import com.dicoding.storyapp.data.mediator.StoryRemoteMediator

class UserRepository(
    private val apiService: ApiService,
    private val userPreference: UserPreference,
    private val storyDatabase: StoryDatabase
) {

    @OptIn(ExperimentalPagingApi::class)
    fun getStories(): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(storyDatabase, apiService),
            pagingSourceFactory = {
                storyDatabase.storyDao().getAllStories()
            }
        ).liveData
    }

    fun getSession() = userPreference.getSession()

    suspend fun getStoryDetail(storyId: String): DetailStoryResponse {
        return apiService.getStoryDetail(storyId)
    }

    suspend fun getStoriesLocation(): StoryResponse{
        return apiService.getStoriesLocation(location = 1)
    }

    suspend fun logout() = userPreference.logout()

    companion object{
        @Volatile
        private var instance: UserRepository?= null
        fun getInstance(apiService: ApiService, userPreference: UserPreference, storyDatabase: StoryDatabase) = UserRepository(apiService, userPreference, storyDatabase)
    }
}