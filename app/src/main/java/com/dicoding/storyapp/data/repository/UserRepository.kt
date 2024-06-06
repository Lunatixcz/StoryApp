package com.dicoding.storyapp.data.repository

import com.dicoding.storyapp.data.api.ApiService
import com.dicoding.storyapp.data.pref.UserPreference
import com.dicoding.storyapp.data.response.DetailStoryResponse
import com.dicoding.storyapp.data.response.StoryResponse

class UserRepository(
    private val apiService: ApiService,
    private val userPreference: UserPreference,
) {
   suspend fun getStories(): StoryResponse{
       return apiService.getStories()
   }

    fun getSession() = userPreference.getSession()

    suspend fun getStoryDetail(storyId: String): DetailStoryResponse {
        return apiService.getStoryDetail(storyId)
    }

    suspend fun logout() = userPreference.logout()

    companion object{
        @Volatile
        private var instance: UserRepository?= null
        fun getInstance(apiService: ApiService, userPreference: UserPreference) = UserRepository(apiService, userPreference)
    }
}